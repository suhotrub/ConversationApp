package com.suhotrub.conversations.ui.activities.finduser

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.PaginationState
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class FindUserPresenter @Inject constructor(
        private val usersInteractor: UsersInteractor
) : MvpPresenter<FindUserView>() {

    var users = mutableListOf<UserDto>()
    var username: String? = null
    var offset: Int = 0

    fun observeUsername(textObservable: Observable<String>) =
            subscribe(textObservable.distinctUntilChanged().debounce(500, TimeUnit.MILLISECONDS)) {

                username = it

                offset = 0
                users.clear()
                viewState.renderUsers(users)
                viewState.setPaginationState(PaginationState.READY)
                loadMore()
            }

    fun loadMore() {
        if (username != null)
            subscribeIoHandleError(usersInteractor.findUsersByLogin(username!!, offset),
                    {
                        users.addAll(it.users)
                        offset++
                        viewState.renderUsers(users)

                        if (it.users.isEmpty())
                            viewState.setPaginationState(PaginationState.COMPLETE)
                    },
                    {
                        viewState.showErrorSnackbar(it)
                    })
        else
            viewState.setPaginationState(PaginationState.COMPLETE)
    }


}