package com.suhotrub.conversations.ui.activities.groupinfo

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.model.user.UsersDto

interface GroupInfoView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderGroup(groupDto: GroupDto)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderUsers(users: List<UserDto>)

}