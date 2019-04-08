package com.suhotrub.conversations.ui.activities.auth.signin

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.suhotrub.conversations.ui.util.recycler.LoadState
import retrofit2.HttpException

interface SignInView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun renderLoadState(loadState: LoadState)

    @StateStrategyType(SkipStrategy::class)
    fun showValidationError()

    @StateStrategyType(SkipStrategy::class)
    fun showErrorSnackbar(t: Throwable)

    @StateStrategyType(SkipStrategy::class)
    fun navigateAfterLogin()


}