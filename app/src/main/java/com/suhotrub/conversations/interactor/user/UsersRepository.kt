package com.suhotrub.conversations.interactor.user

import android.content.Context
import com.suhotrub.conversations.model.user.UserLoginDto
import com.suhotrub.conversations.model.user.UserSignupDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
        val context: Context,
        val usersApi: UsersApi,
        val tokenStorage: TokenStorage
) {

    fun getToken() =
            tokenStorage.getToken()
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
    ).map {
        tokenStorage.setToken(it)
        it
    }

    fun login(
            login: String,
            password: String
    ) = usersApi.login(
            UserLoginDto(
                    login,
                    password
            )
    ).map {
        tokenStorage.setToken(it)
        it
    }

    fun getUsername() =
            usersApi.getUsername()

    fun findUsersByName(name: String, offset: Int) =
            usersApi.getUsersByLogin(name, 20, offset)


}


