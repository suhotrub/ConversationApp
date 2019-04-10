package com.suhotrub.conversations.model.messages

import com.suhotrub.conversations.model.user.UserDto

data class MessageDto(
        val text:String,
        val groupsName:String,
        val time:String,
        val user:UserDto
)