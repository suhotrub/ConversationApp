package com.suhotrub.conversations.ui.activities.auth.signin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.user.TokenStorage
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.ui.util.recycler.LoadState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import retrofit2.HttpException
import javax.inject.Inject

@InjectViewState
@ActivityScope
class SignInPresenter @Inject constructor(
        private val usersInteractor: UsersInteractor,
        private val tokenStorage: TokenStorage
) : MvpPresenter<SignInView>() {

    init {
        tokenStorage.setToken("")
    }

    private val screenModel = SignInScreenModel()

    fun observeLogin(loginObservable: Observable<String>) =
            subscribe(loginObservable.distinctUntilChanged(), screenModel::login::set)

    fun observePassword(passwordObservable: Observable<String>) =
            subscribe(passwordObservable.distinctUntilChanged(), screenModel::password::set)

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
                        viewState.showErrorSnackbar(it)
                        viewState.renderLoadState(screenModel.loadState)
                    }
            )
        }
    }
}