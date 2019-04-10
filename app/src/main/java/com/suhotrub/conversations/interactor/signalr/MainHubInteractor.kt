package com.suhotrub.conversations.interactor.signalr

import android.util.Log
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.suhotrub.conversations.base.di.modules.network.setUnsafe
import com.suhotrub.conversations.interactor.user.TokenStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainHubInteractor @Inject constructor(
        private val tokenStorage: TokenStorage
) {

    private lateinit var hubConnection: HubConnection
    fun createHubConnection() {
        HubConnectionBuilder
                .create("https://176.107.160.64:5000/signalr?access_token=${tokenStorage.getToken()}")
                .build()
                .apply {
                    hubConnection = this

                    val newHttpClient = hubConnection.javaClass.getDeclaredField("httpClient").let { httpClient ->
                        httpClient.isAccessible = true
                        httpClient.get(hubConnection).javaClass.getDeclaredField("client").let {
                            it.isAccessible = true
                            it.get(httpClient.get(hubConnection)) as? OkHttpClient
                        }
                    }?.newBuilder()?.setUnsafe()?.build()

                    hubConnection.javaClass.getDeclaredField("httpClient").let { httpClient ->
                        httpClient.isAccessible = true
                        httpClient.get(hubConnection).javaClass.getDeclaredField("client").let {
                            it.isAccessible = true
                            it.set(httpClient.get(hubConnection), newHttpClient)
                            //it.set(it.get(httpClient.get(hubConnection)), newHttpClient)
                        }
                    }


                    onClosed {
                        hubConnection.start()
                    }
                }
                .start()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    Log.d("HubMessage", "Connected")
                }
                .doOnError {
                    Log.e("HubMessage", it.message, it)
                }
                .subscribe()
    }

    fun getHubConnection() = hubConnection
    fun stopHubConnection() {
        if (::hubConnection.isInitialized) hubConnection.stop()
    }

}