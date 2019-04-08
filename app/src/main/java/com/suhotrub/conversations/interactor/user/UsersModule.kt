package com.suhotrub.conversations.interactor.user

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@Singleton
class UsersModule {

    @Singleton
    @Provides
    fun provideUsersApi(retrofit: Retrofit): UsersApi =
            retrofit.create(UsersApi::class.java)

}