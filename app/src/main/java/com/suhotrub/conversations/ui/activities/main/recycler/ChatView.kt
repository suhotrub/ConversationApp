package com.suhotrub.conversations.ui.activities.main.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.Chat
import com.suhotrub.conversations.model.ChatStatusType
import kotlinx.android.synthetic.main.view_chat.view.*
import java.util.*

class ChatView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_chat, this)
    }

    fun bind(chat: Chat) {

        chat_message.text = chat.message
        chat_name.text = chat.name
        chat_status.visibility = if (chat.statusType == ChatStatusType.ONLINE) View.VISIBLE else View.GONE
        chat_status_text.text = chat.statusText
        chat_avatar.setImageResource(chat.avatar)

        val time = Calendar.getInstance()
        time.time = Date(chat.time)
        val hours = if (time[Calendar.HOUR_OF_DAY] > 10) time[Calendar.HOUR_OF_DAY].toString() else "0" + time[Calendar.HOUR_OF_DAY]
        val minutes = if (time[Calendar.MINUTE] > 10) time[Calendar.MINUTE].toString() else "0" + time[Calendar.MINUTE]

        chat_time.text = "$hours:$minutes"
    }
}