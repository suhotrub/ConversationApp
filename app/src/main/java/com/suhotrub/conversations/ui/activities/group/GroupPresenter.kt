package com.suhotrub.conversations.ui.activities.group

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.interactor.messages.MessagesRepository
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.signalr.invokeEvent
import com.suhotrub.conversations.interactor.user.UsersRepository
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
@ActivityScope
class GroupPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor,
        private val groupDto: GroupDto,
        private val mainHubInteractor: MainHubInteractor,
        private val messagesRepository: MessagesRepository,
        private val usersRepository: UsersRepository
) : MvpPresenter<GroupActivityView>() {

    private var offset: Int = 0
    private var messages = mutableListOf<MessageDto>()
    private var currentMessage: String = ""

    init {
        viewState.renderGroup(groupDto)
        reload()

        subscribeIoHandleError(
                mainHubInteractor.observeEvent("IncomingMessage", MessageDto::class.java),
                { message ->
                    messages.remove(messages.find { it.time == null && it.text == message.text })
                    messages.add(0, message)

                    Log.d("HubMessage", message.text)
                    viewState.renderMessages(messages)
                    viewState.scrollToBottom()
                },
                {
                    Timber.e(it)
                })
    }

    fun observeMessage(textObservable: Observable<String>) =
            subscribe(textObservable, this::currentMessage::set)

    fun sendMessage() {

        mainHubInteractor.safeOperation {
            subscribeIoHandleError(
                    it.invokeEvent<MessageDto>("SendChatMessage", groupDto.groupGuid, currentMessage),
                    {},
                    {
                        viewState.showErrorSnackbar(it)
                    })
        }

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
                    viewState.scrollToBottom()
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