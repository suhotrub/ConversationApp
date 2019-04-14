package com.suhotrub.conversations.ui.activities.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.user.UsersRepository
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import javax.inject.Inject

@InjectViewState
@ActivityScope
class MainPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor,
        private val mainHubInteractor: MainHubInteractor,
        private val usersRepository: UsersRepository
) : MvpPresenter<MainView>() {

    init {
        subscribe(usersRepository.getCurrent()) {}
        mainHubInteractor.stopHubConnection()
        mainHubInteractor.createHubConnection()
    }


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