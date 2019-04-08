package com.suhotrub.conversations.ui.activities.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import retrofit2.HttpException
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor
) : MvpPresenter<MainView>() {

    var offset: Int = 0
    var groups = mutableListOf<GroupDto>()

    fun loadMore() {
        subscribeIoHandleError(groupsInteractor.getJoinedGroups(offset),
                {

                    groups.addAll(it)
                    viewState.renderChats(groups)
                    offset++
                    if (it.isEmpty())
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