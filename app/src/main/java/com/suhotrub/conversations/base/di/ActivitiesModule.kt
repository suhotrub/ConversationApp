package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import com.suhotrub.conversations.ui.activities.auth.signup.SignUpActivity
import com.suhotrub.conversations.ui.activities.call.CallActivity
import com.suhotrub.conversations.ui.activities.call.CallModule
import com.suhotrub.conversations.ui.activities.creategroup.CreateGroupActivity
import com.suhotrub.conversations.ui.activities.finduser.FindUserActivity
import com.suhotrub.conversations.ui.activities.group.GroupActivity
import com.suhotrub.conversations.ui.activities.group.GroupModule
import com.suhotrub.conversations.ui.activities.groupinfo.GroupInfoActivity
import com.suhotrub.conversations.ui.activities.groupinfo.GroupInfoModule
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Модуль для иньекций активностей и их зависимостей
 */
@Module
abstract class ActivitiesModule {

    /**
     * Инжектор SplashActivity
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    /**
     * Инжектор MainActivity
     */
    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity

    /**
     * Инжектор SignInActivty
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSignInActivity(): SignInActivity

    /**
     * Инжектор SignUpActivity
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindSignUpActivity(): SignUpActivity

    /**
     * Инжектор CreateGroupActivity
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindCreateGroupActivity(): CreateGroupActivity

    /**
     * Инжектор FindUserActivity
     */
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindFindUserActivity(): FindUserActivity

    /**
     * Инжектор GroupActivity
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = [(GroupModule::class)])
    abstract fun bindGroupActivity(): GroupActivity

    /**
     * Инжектор GroupInfoActivity
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = [(GroupInfoModule::class)])
    abstract fun bindGroupInfoActivity(): GroupInfoActivity

    /**
     * Инжектор CallActivity
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = [(CallModule::class)])
    abstract fun bindCallActivity(): CallActivity
}