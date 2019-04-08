package com.suhotrub.conversations.ui.activities.group

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import retrofit2.HttpException
import javax.inject.Inject

@InjectViewState
class GroupPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor,
        private val groupDto: GroupDto
) : MvpPresenter<GroupActivityView>() {

    var offset: Int = 0
    var users = mutableListOf<UserDto>()

    init {
        viewState.renderGroup(groupDto)
        reload()
    }

    fun loadMore() {
        subscribeIoHandleError(groupsInteractor.getGroupParticipants(groupDto.name ?: "", offset),
                {

                    users.addAll(it)
                    viewState.renderUsers(users)
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
        users.clear()
        loadMore()
    }

}