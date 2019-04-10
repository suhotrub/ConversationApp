package com.suhotrub.conversations.ui.util.widget.message

import android.view.ViewGroup
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder
import kotlinx.android.synthetic.main.item_message_sent.view.*

class SentMessageItemController(val onClickListener: (MessageDto) -> Unit) : BindableItemController<MessageDto, SentMessageItemController.Holder>() {

    override fun getItemId(data: MessageDto?) =
            (data?.time?.hashCode()?.toLong() ?: 0) +
                    (data?.text?.hashCode()?.toLong() ?: 0)

    override fun createViewHolder(parent: ViewGroup?) = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<MessageDto>(parent, R.layout.item_message_sent) {
        override fun bind(data: MessageDto?) {
            data?.let {
                itemView.message_view.bind(data)
            }
        }
    }
}