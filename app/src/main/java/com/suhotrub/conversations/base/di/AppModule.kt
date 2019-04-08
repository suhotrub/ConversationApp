package com.suhotrub.conversations.base.di

import android.content.Context
import com.suhotrub.conversations.base.App
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module
class AppModule(app: App) {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

}