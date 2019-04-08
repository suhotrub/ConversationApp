package com.suhotrub.conversations.ui.activities.main

import android.app.Activity
import dagger.Binds
import dagger.BindsInstance
import dagger.Module

@Module
abstract class MainModule {

    @Binds
    abstract fun provideActivity(activity: MainActivity): Activity
}