package com.suhotrub.conversations.ui.activities.call

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

class SDPCreateCallback(private val callback: (SDPCreateResult) -> Unit) : SdpObserver {

    override fun onSetFailure(reason: String?) {}

    override fun onSetSuccess() {}

    override fun onCreateSuccess(descriptor: SessionDescription) = callback(SDPCreateSuccess(descriptor))

    override fun onCreateFailure(reason: String?) = callback(SDPCreateFailure(reason))
}