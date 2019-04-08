package com.suhotrub.conversations.ui.activities.logout

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.ui.activities.auth.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_logout.*

class LogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logout)
        StatusBarUtil.setTransparent(this)

        cancel_btn.setOnClickListener {
            onBackPressed()
        }

        logout_btn.setOnClickListener {
            startActivity(
                    Intent(this@LogoutActivity, SignInActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finishAffinity()
        }
    }
}