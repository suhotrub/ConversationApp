package com.suhotrub.conversations.ui.activities.group

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.model.group.GroupDto
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class GroupModule {

    @Provides
    fun provideGroupDto(activity:GroupActivity) =
            activity.intent.getParcelableExtra<GroupDto>("EXTRA_FIRST") ?: GroupDto()

}