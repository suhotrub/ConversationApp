package com.suhotrub.conversations.interactor.groups

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@Singleton
class GroupsModule {

    @Provides
    fun provideGroupsApi(retrofit: Retrofit) =
            retrofit.create(GroupsApi::class.java)

}