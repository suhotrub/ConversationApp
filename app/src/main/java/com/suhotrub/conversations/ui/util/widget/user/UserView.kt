package com.suhotrub.conversations.ui.util.widget.user

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import kotlinx.android.synthetic.main.view_user.view.*

class UserView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_user, this)
    }

    fun bind(user: UserDto) {

        user_avatar.setImageResource(R.drawable.bg_placeholder_circle)
        user_mail.setTextOrGone(user.login)
        user_name.setTextOrGone(user.name + " " + user.surname)
    }
}