package com.suhotrub.conversations.model.webrtc

import org.webrtc.SessionDescription

data class SDPCreateSuccess(val descriptor: SessionDescription) : SDPCreateResult