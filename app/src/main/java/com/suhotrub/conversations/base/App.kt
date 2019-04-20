package com.suhotrub.conversations.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.suhotrub.conversations.base.di.AppComponent
import com.suhotrub.conversations.base.di.AppModule
import com.suhotrub.conversations.base.di.DaggerAppComponent
import com.suhotrub.conversations.base.di.modules.network.FireBaseToken
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

/**
 * Приложение
 */
class App : DaggerApplication() {

    /**
     * Возвращает ижектор для активностей
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build().apply { inject(this@App) }
        return appComponent
    }

    /**
     * Компонент приложения
     */
    lateinit var appComponent: AppComponent

    /**
     * Текущая активносить
     */
    var currentActivity: Activity? = null

    /**
     * Вызывается при создании приложения
     */
    override fun onCreate() {
        super.onCreate()

        initTimber()

        RxJavaPlugins.setErrorHandler {
            Timber.e("RxError: $it")
        }


        initActivityLifeycleCallbacks()
        initFirebase()
    }

    /**
     * Инициализирует Timber
     */
    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    /**
     * Инициализирует Firebase
     */
    private fun initFirebase(){
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result?.token
                    FireBaseToken.token = token
                    Log.d("FCM", "token $token")
                })
    }

    /**
     * Подписывается на жизненый цикл активностей
     */
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