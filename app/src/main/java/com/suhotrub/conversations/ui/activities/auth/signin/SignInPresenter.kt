package com.suhotrub.conversations.ui.activities.auth.signin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.ui.util.recycler.LoadState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import javax.inject.Inject

@InjectViewState
@ActivityScope
class SignInPresenter @Inject constructor(
        private val usersInteractor: UsersInteractor
) : MvpPresenter<SignInView>() {

    val screenModel = SignInScreenModel()

    fun observeLogin(loginObservable: Observable<String>) =
            subscribe(loginObservable.distinctUntilChanged()) {
                screenModel.login = it
            }

    fun observePassword(passwordObservable: Observable<String>) =
            subscribe(passwordObservable.distinctUntilChanged()) {
                screenModel.password = it
            }

    private fun validateFields(): Boolean {
        if (screenModel.login.isNullOrBlank() || screenModel.password.isNullOrBlank())
            viewState.showValidationError()
        else
            return true
        return false
    }

    fun signIn() {
        if (validateFields()) {
            screenModel.loadState = LoadState.MAIN_LOADING
            viewState.renderLoadState(screenModel.loadState)

            subscribeIoHandleError(usersInteractor.login(screenModel.login.orEmpty(), screenModel.password.orEmpty()),
                    {
                        viewState.navigateAfterLogin()
                    },
                    {
                        screenModel.loadState = LoadState.NONE
                        viewState.showError(it)
                        viewState.renderLoadState(screenModel.loadState)
                    }
            )
        }
    }
}