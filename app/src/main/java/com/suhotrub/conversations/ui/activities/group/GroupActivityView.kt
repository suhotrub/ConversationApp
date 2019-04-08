package com.suhotrub.conversations.ui.activities.group

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import retrofit2.HttpException

interface GroupActivityView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderUsers(users: List<UserDto>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderGroup(groupDto: GroupDto)

    @StateStrategyType(SkipStrategy::class)
    fun showErrorSnackbar(t: Throwable)

    @StateStrategyType(SkipStrategy::class)
    fun setPaginationState(paginationState: PaginationState)
}