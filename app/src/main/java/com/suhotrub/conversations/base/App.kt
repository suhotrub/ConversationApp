package com.suhotrub.conversations.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
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
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        initTimber()

        RxJavaPlugins.setErrorHandler {
            Timber.e("RxError: $it")
        }


        initActivityLifeycleCallbacks()
    }

    private fun initTimber() {
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

    fun initActivityLifeycleCallbacks() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }
        })
    }
}