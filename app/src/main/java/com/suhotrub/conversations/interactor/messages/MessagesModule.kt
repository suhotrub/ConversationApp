package com.suhotrub.conversations.interactor.messages

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@Singleton
class MessagesModule() {

    @Provides
    @Singleton
    fun provideMessagesApi(retrofit: Retrofit) =
            retrofit.create(MessagesApi::class.java)
}