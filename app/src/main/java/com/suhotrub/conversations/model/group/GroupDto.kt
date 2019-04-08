package com.suhotrub.conversations.model.group

import com.suhotrub.conversations.model.ChatStatusType

data class GroupDto(
        val id: Long,
        val name:String,
        val message:String,
        val avatar:Int,
        val statusText:String,
        val statusType: ChatStatusType,
        val time: Long
)

