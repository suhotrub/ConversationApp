package com.suhotrub.conversations.interactor.user

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersInteractor @Inject constructor(
        val usersRepository: UsersRepository
) {

    fun isLoggedIn() = usersRepository.isLoggedIn()

    fun signUp(
            login: String,
            password: String,
            name: String,
            surname: String
    ) = usersRepository.signUp(
            login,
            password,
            name,
            surname
    )

    fun login(
            login: String,
            password: String
    ) = usersRepository.login(
            login,
            password
    )

    fun getUsername() = usersRepository.getUsername()

    fun findUsersByLogin(
            login: String,
            offset: Int
    ) = usersRepository.findUsersByName(
            login,
            offset
    )


    fun getCurrent() =
            usersRepository.getCurrent()
}