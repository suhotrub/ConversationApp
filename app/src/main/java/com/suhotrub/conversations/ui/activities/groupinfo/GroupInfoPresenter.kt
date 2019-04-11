package com.suhotrub.conversations.ui.activities.groupinfo

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.subscribe
import javax.inject.Inject

@InjectViewState
class GroupInfoPresenter @Inject constructor(
        private val groupDto: GroupDto,
        private val groupsInteractor: GroupsInteractor,
        private val usersInteractor: UsersInteractor
) : MvpPresenter<GroupInfoView>() {

    init {

        viewState.renderGroup(groupDto)

        subscribe(groupsInteractor.getGroupParticipants(groupDto.groupGuid, 0)) {
            viewState.renderUsers(it.users)
        }

    }

}