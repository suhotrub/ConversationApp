package com.suhotrub.conversations.interactor.user

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage @Inject constructor(
        val context: Context
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
                    .commit().also {
                        userToken = token
                    }

    fun getToken() =
            if (::userToken.isInitialized)
                userToken.takeIf { it.isNotBlank() }
            else
                sharedPreferences.getString("user_token", "").also {
                    userToken = it ?: ""
                }
}