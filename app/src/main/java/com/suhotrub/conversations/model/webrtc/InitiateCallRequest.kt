package com.suhotrub.conversations.model.webrtc

data class InitiateCallRequest(
        val offer: Jsep,
        val groupGuid: String
)