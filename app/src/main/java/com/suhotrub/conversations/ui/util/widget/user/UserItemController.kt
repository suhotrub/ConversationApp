package com.suhotrub.conversations.ui.util.widget.user

import android.view.ViewGroup
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.recycler.controller.BindableItemController
import com.suhotrub.conversations.ui.util.recycler.holder.BindableViewHolder
import com.suhotrub.conversations.ui.util.ui.setVisibleOrGone
import kotlinx.android.synthetic.main.item_user.view.*

class UserItemController(val onClickListener: (UserDto) -> Unit, val isDeleting: Boolean = false) : BindableItemController<UserDto, UserItemController.Holder>() {

    override fun getItemId(data: UserDto?) = data?.login.hashCode().toLong() ?: 0

    override fun createViewHolder(parent: ViewGroup?) = Holder(parent)

    inner class Holder(parent: ViewGroup?) : BindableViewHolder<UserDto>(parent, R.layout.item_user) {
        override fun bind(data: UserDto?) {
            data?.let {
                itemView.user_view.bind(data)
                itemView.user_clear_iv.setVisibleOrGone(isDeleting)
            }
            itemView.root.setOnClickListener {
                data?.let(onClickListener)
            }
        }
    }
}