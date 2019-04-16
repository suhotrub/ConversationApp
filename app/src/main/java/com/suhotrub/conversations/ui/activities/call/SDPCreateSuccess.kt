package com.suhotrub.conversations.ui.activities.call

import org.webrtc.SessionDescription

data class SDPCreateSuccess(val descriptor: SessionDescription) : SDPCreateResult