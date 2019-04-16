package com.suhotrub.conversations.ui.activities.call

data class AnswerNewPublisherRequest(
        val handleId: Long,
        val answer: Jsep
)