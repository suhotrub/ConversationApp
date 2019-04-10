package com.suhotrub.conversations.model.messages

import com.suhotrub.conversations.model.user.UserDto

data class MessageDto(
        val text:String,
        val groupName:String,
        val time:String?,
        val user:UserDto
)