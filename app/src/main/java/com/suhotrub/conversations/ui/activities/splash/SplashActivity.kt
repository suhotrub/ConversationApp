package com.suhotrub.conversations.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.util.subscribeIoHandleError
import dagger.android.AndroidInjection
import retrofit2.HttpException
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var usersInteractor: UsersInteractor
    @Inject
    lateinit var mainHubInteractor: MainHubInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        setContentView(R.layout.activity_splash)
        StatusBarUtil.setTransparent(this)


        subscribeIoHandleError(
                usersInteractor.getCurrent().retry { t -> (t as? HttpException)?.code() != 401 },
                {
                    if (usersInteractor.isLoggedIn())
                        startActivity(Intent(this, MainActivity::class.java))
                    else
                        startActivity(Intent(this, SignInActivity::class.java))
                    finish()

                },
                {
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                })

    }
}