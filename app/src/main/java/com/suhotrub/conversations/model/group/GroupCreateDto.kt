package com.suhotrub.conversations.model.group

data class GroupCreateDto(
        val name:String,
        val users:List<String>,
        val description:String?,
        val avatarLink:String
)

