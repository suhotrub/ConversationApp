package com.suhotrub.conversations.model.webrtc

data class InititateCallResponse(
        val error: Int?,
        val data: Jsep
)