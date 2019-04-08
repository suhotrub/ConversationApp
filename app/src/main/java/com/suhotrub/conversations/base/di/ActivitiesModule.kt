package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import com.suhotrub.conversations.ui.activities.auth.signup.SignUpActivity
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
}