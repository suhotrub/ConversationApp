package com.suhotrub.conversations.ui.activities.call

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.model.group.GroupDto
import dagger.Module
import dagger.Provides

@Module
@ActivityScope
class CallModule {
    @Provides
    @ActivityScope
    fun provideGroupDto(callActivity: CallActivity): GroupDto =
            callActivity.intent.getParcelableExtra("EXTRA_FIRST") ?: GroupDto()
}