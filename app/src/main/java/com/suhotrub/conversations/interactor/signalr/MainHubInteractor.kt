package com.suhotrub.conversations.interactor.signalr

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.suhotrub.conversations.base.di.modules.network.setUnsafe
import com.suhotrub.conversations.interactor.user.TokenStorage
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.model.messages.MessageDto
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainHubInteractor @Inject constructor(
        private val tokenStorage: TokenStorage,
        private val gson: Gson
) {

    lateinit var hubConnection: HubConnection
    fun initHubConnection() = {
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
                            it.set(httpClient.get(hubConnection),newHttpClient)
                            //it.set(it.get(httpClient.get(hubConnection)), newHttpClient)
                        }
                    }


                    onClosed {
                        hubConnection.start()
                    }
                    on("IncomingMessage", {
                        Log.d("HubMessage", it.text)
                    }, MessageDto::class.java)
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

    /*fun <T> subscribeToEvent(name: String): Observable<T> {
        val observable = PublishSubject.create<T>()
        val eventListener = HubEventListener {
            observable.onNext(gson.fromJson<T>(it.toString(), object : TypeToken<T>() {}.type))
        }

        observable.doOnDispose { hubConnection.unSubscribeFromEvent(name, eventListener) }
        hubConnection.subscribeToEvent(name, eventListener)

        return observable
    }

    fun sendEvent(name: String, vararg params: Any) {
        hubConnection.addListener(object : HubConnectionListener {
            override fun onConnected() {
                Log.d("HubMessage", "Connected")
                hubConnection.invoke(name, params)
            }

            override fun onMessage(message: HubMessage?) {
                Log.d("HubMessage", message.toString())
            }

            override fun onDisconnected() {
                Log.d("HubMessage", "Disconnected")

            }

            override fun onError(exception: Exception?) {
                Log.e("HubMessage", exception.toString(),exception)
            }
        })
        //if(!hubConnection.isConnected)
    }*/
}