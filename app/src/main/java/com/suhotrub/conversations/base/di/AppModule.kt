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

/**
 * Модуль приложения
 * @param app приложение
 */
@Module
class AppModule(app: App) {

    /**
     * Возвращает контекст приложения
     * @param application приложение
     */
    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

}