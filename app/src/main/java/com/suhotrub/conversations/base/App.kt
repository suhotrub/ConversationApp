package com.suhotrub.conversations.base

import com.suhotrub.conversations.base.di.AppComponent
import com.suhotrub.conversations.base.di.AppModule
import com.suhotrub.conversations.base.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build().apply { inject(this@App) }
        return appComponent
    }

    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()

        initTimber()

        RxJavaPlugins.setErrorHandler {
            Timber.e("RxError: $it")
        }

    }

    private fun initTimber(){
        Timber.plant(Timber.DebugTree())
    }


   /* override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }*/
}