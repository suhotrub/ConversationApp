package com.suhotrub.conversations.ui.activities.call

import com.suhotrub.conversations.model.user.UserDto

data class NewPublisherResponse(
        val handleId: Long,
        val answer: Jsep,
        val user: UserDto
)