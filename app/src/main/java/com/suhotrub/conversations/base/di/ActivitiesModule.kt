package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import com.suhotrub.conversations.ui.activities.auth.signup.SignUpActivity
import com.suhotrub.conversations.ui.activities.creategroup.CreateGroupActivity
import com.suhotrub.conversations.ui.activities.finduser.FindUserActivity
import com.suhotrub.conversations.ui.activities.group.GroupActivity
import com.suhotrub.conversations.ui.activities.group.GroupModule
import com.suhotrub.conversations.ui.activities.groupinfo.GroupInfoActivity
import com.suhotrub.conversations.ui.activities.groupinfo.GroupInfoModule
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

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSignInActivity(): SignInActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSignUpActivity(): SignUpActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCreateGroupActivity(): CreateGroupActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindFindUserActivity(): FindUserActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(GroupModule::class)])
    abstract fun bindGroupActivity(): GroupActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(GroupInfoModule::class)])
    abstract fun bindGroupInfoActivity(): GroupInfoActivity
}