package com.suhotrub.conversations.model.webrtc

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

class SDPSetCallback(private val callback: (String?) -> Unit) : SdpObserver {

    override fun onSetFailure(reason: String?) = callback(reason)

    override fun onSetSuccess() = callback(null)

    override fun onCreateSuccess(descriptor: SessionDescription?) {}

    override fun onCreateFailure(reason: String?) {}
}