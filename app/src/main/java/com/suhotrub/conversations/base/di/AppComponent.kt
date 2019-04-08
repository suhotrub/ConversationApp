package com.suhotrub.conversations.base.di

import com.suhotrub.conversations.base.App
import com.suhotrub.conversations.base.di.modules.network.NetworkModule
import com.suhotrub.conversations.base.di.modules.network.OkHttpModule
import com.suhotrub.conversations.interactor.groups.GroupsModule
import com.suhotrub.conversations.interactor.user.UsersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidInjectionModule::class),
    (AndroidSupportInjectionModule::class),
    (AppModule::class),
    (NetworkModule::class),
    (OkHttpModule::class),
    (ActivitiesModule::class),
    (UsersModule::class),
    (GroupsModule::class)

])

interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

}