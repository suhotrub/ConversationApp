package com.suhotrub.conversations.ui.activities.auth.signin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.ui.activities.auth.signup.SignUpActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : MvpAppCompatActivity() {

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        StatusBarUtil.setTransparent(this)

        textView3.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        button.setOnClickListener {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
}