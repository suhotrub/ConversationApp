package com.suhotrub.conversations.ui.activities.creategroup

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.model.user.UserDto
import retrofit2.HttpException

interface CreateGroupView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderUsers(users: List<UserDto>)

    @StateStrategyType(SkipStrategy::class)
    fun showErrorSnackbar(t: Throwable)

    @StateStrategyType(SkipStrategy::class)
    fun showErrorSnackbar(text: String)

    @StateStrategyType(SkipStrategy::class)
    fun finish()
}