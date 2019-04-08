package com.suhotrub.conversations.ui.activities.main.recycler

import android.view.ViewGroup
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder
import kotlinx.android.synthetic.main.item_chat.view.*

class GroupItemController : BindableItemController<GroupDto, GroupItemController.Holder>() {

    override fun getItemId(data: GroupDto?) = data?.id ?: 0

    override fun createViewHolder(parent: ViewGroup?) = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<GroupDto>(parent, R.layout.item_chat) {
        override fun bind(data: GroupDto?) {
            data?.let {
                itemView.chat_view.bind(data)
            }
        }
    }
}