package com.suhotrub.conversations.ui.activities.main.recycler

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.toHHmm
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import com.suhotrub.conversations.ui.util.ui.setVisibleOrGone
import kotlinx.android.synthetic.main.view_group.view.*

class GroupView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_group, this)
    }

    fun bind(chat: GroupDto) {

        chat_message.setTextOrGone("${chat.lastMessage?.user?.login}: ${chat.lastMessage?.text}")
        chat_time.setTextOrGone(chat.lastMessage?.time?.toHHmm())

        chat_name.setTextOrGone(chat.name)
        chat_status.setVisibleOrGone(chat.inCall==true)
        //chat_status_text.setTextOrGone(chat.description)

        chat_avatar.setImageResource(R.drawable.bg_placeholder_circle)

        //val time = Calendar.getInstance()
        //time.time = Date(chat.time)
        //val hours = if (time[Calendar.HOUR_OF_DAY] > 10) time[Calendar.HOUR_OF_DAY].toString() else "0" + time[Calendar.HOUR_OF_DAY]
        //val minutes = if (time[Calendar.MINUTE] > 10) time[Calendar.MINUTE].toString() else "0" + time[Calendar.MINUTE]

        //chat_time.text = "$hours:$minutes"
    }
}