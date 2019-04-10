package com.suhotrub.conversations.ui.activities.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.suhotrub.conversations.base.di.modules.network.setUnsafe
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.user.UsersRepository
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor,
        private val mainHubInteractor: MainHubInteractor,
        private val usersRepository: UsersRepository
) : MvpPresenter<MainView>() {

    init {

        /*WebSocketHubConnectionP2(, usersRepository.getToken())
                .apply{
                    addListener(object : HubConnectionListener {
                        override fun onConnected() {
                            Log.d("HubMessage", "Connected")
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
                    subscribeToEvent("IncomingMessage"){
                        Log.d("IncomingMessage",it.toString())
                    }
                    connect()
                }*/
    }

    /*init {
        subscribeIoHandleError(mainHubInteractor.subscribeToEvent<MessageDto>("IncomingMessage"),
                {
                    Log.d("kek", it.text)
                },
                {
                    Log.e("kek", it.message, it)
                }
        )
        mainHubInteractor.sendEvent("SendChatMessage", "lol", "kik")

    }*/

    var offset: Int = 0
    var groups = mutableListOf<GroupDto>()

    fun loadMore() {
        subscribeIoHandleError(groupsInteractor.getJoinedGroups(offset),
                {

                    groups.addAll(it.groups)
                    viewState.renderChats(groups)
                    offset++
                    if (it.groups.isEmpty())
                        viewState.setPaginationState(PaginationState.COMPLETE)
                },
                {
                    viewState.showErrorSnackbar(it)
                    viewState.setPaginationState(PaginationState.ERROR)
                })
    }

    fun reload() {
        offset = 0
        groups.clear()
        loadMore()
    }

}