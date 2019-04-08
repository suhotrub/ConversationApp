package com.suhotrub.conversations.ui.activities.auth.signup

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.ui.activities.auth.signin.SignInView
import com.suhotrub.conversations.ui.util.recycler.LoadState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import javax.inject.Inject

@ActivityScope
@InjectViewState
class SignUpPresenter @Inject constructor(
        private val usersInteractor: UsersInteractor
) : MvpPresenter<SignUpView>() {

    val screenModel = SignUpScreenModel()

    fun observeLogin(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged()) {
                screenModel.login = it
            }

    fun observePassword(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged()) {
                screenModel.password = it
            }

    fun observePasswordAgain(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged()) {
                screenModel.passwordAgain = it
            }

    fun observeName(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged()) {
                screenModel.name = it
            }

    fun observeSurname(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged()) {
                screenModel.surname = it
            }

    private fun validateFields(): Boolean {
        if (
                screenModel.login.isNullOrBlank()
                || screenModel.password.isNullOrBlank()
                || screenModel.passwordAgain.isNullOrBlank()
                || screenModel.name.isNullOrBlank()
                || screenModel.surname.isNullOrBlank()
                || screenModel.passwordAgain != screenModel.password
        )
            viewState.showValidationError()
        else
            return true
        return false
    }

    fun signUp() {
        if (validateFields()) {
            screenModel.loadState = LoadState.MAIN_LOADING
            viewState.renderLoadState(screenModel.loadState)

            subscribeIoHandleError(usersInteractor.signUp(
                    screenModel.login.orEmpty(),
                    screenModel.password.orEmpty(),
                    screenModel.name.orEmpty(),
                    screenModel.surname.orEmpty()),
                    {
                        viewState.navigateAfterLogin()
                    },
                    {
                        screenModel.loadState = LoadState.NONE

                        viewState.renderLoadState(screenModel.loadState)
                        viewState.showError(it)
                    }
            )
        }
    }
}