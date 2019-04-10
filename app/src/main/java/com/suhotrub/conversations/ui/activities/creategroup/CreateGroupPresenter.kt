package com.suhotrub.conversations.ui.activities.creategroup

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.interactor.groups.GroupsInteractor
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.subscribe
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import io.reactivex.Observable
import javax.inject.Inject

@InjectViewState
class CreateGroupPresenter @Inject constructor(
        private val groupsInteractor: GroupsInteractor
) : MvpPresenter<CreateGroupView>() {

    var users = mutableSetOf<UserDto>()
    var title: String? = null
    var description: String? = null

    fun deleteUser(user: UserDto) {
        users.remove(user)
        viewState.renderUsers(users.toList())
    }

    fun addUser(user: UserDto) {
        users.add(user)
        viewState.renderUsers(users.toList())
    }

    fun observeGroupTitle(textObservable: Observable<String>) =
            subscribe(textObservable, this::title::set)

    fun observeGroupDescription(textObservable: Observable<String>) =
            subscribe(textObservable, this::description::set)

    private fun validateField() =
            if (users.isEmpty() || title.isNullOrEmpty())
                false.also { viewState.showErrorSnackbar("Проверьте правильность полей") }
            else
                true

    fun submit() {
        if (validateField()) {
            subscribeIoHandleError(groupsInteractor.createGroup(title!!, description, "", users.map(UserDto::userGuid)),
                    {
                        viewState.finish()
                    },
                    {
                        viewState.showErrorSnackbar(it)
                    }
            )
        }
    }
}