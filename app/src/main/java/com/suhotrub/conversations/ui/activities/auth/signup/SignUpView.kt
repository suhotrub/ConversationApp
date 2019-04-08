package com.suhotrub.conversations.ui.activities.auth.signup

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.ui.util.recycler.LoadState

interface SignUpView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderLoadState(loadState: LoadState)

    @StateStrategyType(SkipStrategy::class)
    fun showValidationError()

    @StateStrategyType(SkipStrategy::class)
    fun showError(t:Throwable)

    @StateStrategyType(SkipStrategy::class)
    fun navigateAfterLogin()


}