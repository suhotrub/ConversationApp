package com.suhotrub.conversations.ui.util.widget.message

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.parseISOString
import com.suhotrub.conversations.ui.util.toHHmm
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import kotlinx.android.synthetic.main.view_message_received.view.*

class MessageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_message_received, this)
    }

    fun bind(message: MessageDto) {
        message_user_name.setTextOrGone("${message.user?.name} ${message.user?.surname}")
        message_text.setTextOrGone(message.text)
        message_time.setTextOrGone(message.time?.toHHmm())
    }
}