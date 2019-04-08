package com.suhotrub.conversations.ui.activities.auth.signup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.suhotrub.conversations.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)



        textView3.setOnClickListener {
            onBackPressed()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}