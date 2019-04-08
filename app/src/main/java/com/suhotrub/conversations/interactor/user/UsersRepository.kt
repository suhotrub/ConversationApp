package com.suhotrub.conversations.interactor.user

import android.content.Context
import com.suhotrub.conversations.model.user.UserLoginDto
import com.suhotrub.conversations.model.user.UserSignupDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
        val context: Context,
        val usersApi: UsersApi
) {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("UsersPreferences", Context.MODE_PRIVATE)
    }

    private lateinit var userToken: String

    fun isLoggedIn() = !getToken().isNullOrBlank()

    fun setToken(token: String) =
            sharedPreferences
                    .edit()
                    .putString("user_token", token)
                    .apply().also {
                        userToken = token
                    }

    fun getToken() =
            if (::userToken.isInitialized)
                userToken.takeIf { it.isNotBlank() }
            else
                sharedPreferences.getString("user_token", "").also {
                    userToken = it ?: ""
                }


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
        setToken(it)
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
        setToken(it)
    }

    fun getUsername() =
            usersApi.getUsername()

    fun findUsersByName(name: String, offset: Int) =
            usersApi.getUsersByLogin(name, 20, offset)


}