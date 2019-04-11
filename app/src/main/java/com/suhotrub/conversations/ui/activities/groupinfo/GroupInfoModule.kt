package com.suhotrub.conversations.ui.activities.groupinfo

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.model.group.GroupDto
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class GroupInfoModule {
    @Provides
    @ActivityScope
    fun provideGroupDto(groupInfoActivity: GroupInfoActivity): GroupDto =
            groupInfoActivity.intent.getParcelableExtra("EXTRA_FIRST") ?: GroupDto()
}