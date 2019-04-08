package com.suhotrub.conversations.interactor.user

import dagger.Module
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UsersModule {

    fun provideUsersApi(retrofit: Retrofit) =
            retrofit.create(UsersApi::class.java)

}