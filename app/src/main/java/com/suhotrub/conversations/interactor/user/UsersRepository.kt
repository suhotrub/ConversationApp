package com.suhotrub.conversations.interactor.user

import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.model.user.UserLoginDto
import com.suhotrub.conversations.model.user.UserSignupDto
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
        private val usersApi: UsersApi,
        private val tokenStorage: TokenStorage,
        private val mainHubInteractor: MainHubInteractor
) {
    private var currentUser: UserDto? = null

    fun getCurrentUser() = currentUser

    fun isLoggedIn() =
            tokenStorage.isLoggedIn()

    fun signUp(
            login: String,
            password: String,
            name: String,
            surname: String
    ) = usersApi.signUp(
            UserSignupDto(
                    login,
                    password,
                    name,
                    surname
            )
    ).doOnNext {
        currentUser = null
        tokenStorage.setToken(it)
        mainHubInteractor.createHubConnection()

    }

    fun login(
            login: String,
            password: String
    ) = usersApi.login(
            UserLoginDto(
                    login,
                    password
            )
    ).doOnNext {
        currentUser = null
        tokenStorage.setToken(it)
        mainHubInteractor.createHubConnection()
    }

    fun getUsername() =
            usersApi.getUsername()

    fun findUsersByName(name: String, offset: Int) =
            usersApi.getUsersByLogin(name, 20, offset)

    fun getCurrent() =
            usersApi.getCurrentUser()
                    .doOnNext {
                        currentUser = it
                    }
                    .doOnError {
                        if ((it as? HttpException)?.code() == 401) {
                            tokenStorage.setToken("")
                            mainHubInteractor.stopHubConnection()
                        }
                    }

}


