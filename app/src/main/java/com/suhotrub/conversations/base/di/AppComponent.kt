package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.App
import com.suhotrub.conversations.base.di.modules.network.NetworkModule
import com.suhotrub.conversations.base.di.modules.network.OkHttpModule
import com.suhotrub.conversations.interactor.groups.GroupsModule
import com.suhotrub.conversations.interactor.messages.MessagesModule
import com.suhotrub.conversations.interactor.user.UsersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Компонент приложения
 */
@Singleton
@Component(modules = [
    (AndroidInjectionModule::class),
    (AndroidSupportInjectionModule::class),
    (AppModule::class),
    (NetworkModule::class),
    (OkHttpModule::class),
    (ActivitiesModule::class),
    (UsersModule::class),
    (GroupsModule::class),
    (MessagesModule::class)
])
interface AppComponent : AndroidInjector<App> {

    /**
     * Интерфейс билдера компонента приложения
     */
    @Component.Builder
    interface Builder {

        /**
         * Привязывает объект приложения к компоненту
         * @param application приложение
         */
        @BindsInstance
        fun application(application: App): Builder

        /**
         * Привязывает модуль приложения к компоненту приложения
         * @param appModule модуль приложения
         */
        fun appModule(appModule: AppModule): Builder

        /**
         * Конструирует компонент и возвращает его
         */
        fun build(): AppComponent
    }

}