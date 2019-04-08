package com.suhotrub.conversations.interactor.groups

import dagger.Module
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class GroupsModule {

    fun provideGroupsApi(retrofit: Retrofit) =
            retrofit.create(GroupsApi::class.java)

}