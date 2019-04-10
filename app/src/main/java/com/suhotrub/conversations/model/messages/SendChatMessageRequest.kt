package com.suhotrub.conversations.model.messages

data class SendChatMessageRequest(
        val Text: String,
        val GroupGuid: String
)