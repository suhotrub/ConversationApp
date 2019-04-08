package com.suhotrub.conversations.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.interactor.user.UsersInteractor
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var usersInteractor: UsersInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        setContentView(R.layout.activity_splash)
        StatusBarUtil.setTransparent(this)

        if (usersInteractor.isLoggedIn())
            startActivity(Intent(this, MainActivity::class.java))
        else
            startActivity(Intent(this, SignInActivity::class.java))

        finish()
    }
}