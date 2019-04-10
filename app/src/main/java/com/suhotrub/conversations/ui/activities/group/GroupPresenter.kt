package com.suhotrub.conversations.ui.activities.group

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.interactor.messages.MessagesRepository
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.user.UsersRepository
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import javax.inject.Inject

@InjectViewState
class GroupPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor,
        private val groupDto: GroupDto,
        private val mainHubInteractor: MainHubInteractor,
        private val messagesRepository: MessagesRepository,
        private val usersRepository: UsersRepository
) : MvpPresenter<GroupActivityView>() {

    var offset: Int = 0
    var messages = mutableListOf<MessageDto>()
    var currentMessage: String = ""

    init {
        viewState.renderGroup(groupDto)
        reload()

        mainHubInteractor.getHubConnection()
                .on("IncomingMessage", { message ->
                    messages.remove(messages.find { it.time == null && it.text == message.text })
                    messages.add(0, message)

                    subscribe(Observable.just(message)) {
                        Log.d("HubMessage", message.text)
                        viewState.renderMessages(messages)
                        viewState.scrollToBottom()
                    }
                }, MessageDto::class.java)
    }

    fun observeMessage(textObservable: Observable<String>) =
            subscribe(textObservable, this::currentMessage::set)

    fun sendMessage() {
        val sentMessage = currentMessage
        subscribeIoHandleError(mainHubInteractor.getHubConnection()
                .invoke(MessageDto::class.java, "SendChatMessage",
                        groupDto.groupGuid, currentMessage).toObservable(),
                {

                    /*messages.add(0, it)*/
                    subscribe(Observable.just(it)) {
                        Log.d("HubMessage", it.text)
                        //viewState.renderMessages(messages)
                    }
                },
                {
                    viewState.showErrorSnackbar(it)
                })

        /*mainHubInteractor
                .getHubConnection()
                .send("SendChatMessage", SendChatMessageRequest(currentMessage,groupDto.groupGuid))
        */
        messages.add(0, MessageDto(currentMessage, groupDto.name
                ?: "", null, usersRepository.getCurrentUser()!!))
        viewState.scrollToBottom()

        viewState.renderMessages(messages)
        viewState.clearMessage()
    }

    fun loadMore() {
        subscribeIoHandleError(messagesRepository.getGroupMessages(groupDto.groupGuid, offset),
                {

                    messages.addAll(it.messages)
                    viewState.renderMessages(messages)
                    offset++
                    if (it.messages.isEmpty())
                        viewState.setPaginationState(PaginationState.COMPLETE)
                },
                {
                    viewState.showErrorSnackbar(it)
                    viewState.setPaginationState(PaginationState.ERROR)
                })

    }

    fun reload() {
        offset = 0
        messages.clear()
        loadMore()
    }

}