package com.suhotrub.conversations.model.webrtc

data class TrickleCandidateReceived(
        val sdpMid: String,
        val sdpMLineIndex: Int,
        val candidate: String,
        val completed: Boolean,
        val handleId: Long?
)