package com.suhotrub.conversations.model.webrtc

import com.suhotrub.conversations.model.user.UserDto

data class NewPublisherResponse(
        val handleId: Long?,
        val answer: Jsep?,
        val user: UserDto?
)