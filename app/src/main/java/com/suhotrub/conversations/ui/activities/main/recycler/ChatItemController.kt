package com.suhotrub.conversations.ui.activities.main.recycler

import android.view.ViewGroup
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.Chat
import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatItemController : BindableItemController<Chat, ChatItemController.Holder>() {

    override fun getItemId(data: Chat?) = data?.id ?: 0

    override fun createViewHolder(parent: ViewGroup?) = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<Chat>(parent, R.layout.item_chat) {
        override fun bind(data: Chat?) {
            data?.let {
                itemView.chat_view.bind(data)
            }
        }
    }
}