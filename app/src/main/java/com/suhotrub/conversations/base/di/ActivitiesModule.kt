package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.activities.main.MainModule
import com.suhotrub.conversations.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun bindMainActivity(): MainActivity

}