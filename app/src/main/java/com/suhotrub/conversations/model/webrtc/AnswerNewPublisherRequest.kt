package com.suhotrub.conversations.model.webrtc

data class AnswerNewPublisherRequest(
        val handleId: Long,
        val answer: Jsep
)