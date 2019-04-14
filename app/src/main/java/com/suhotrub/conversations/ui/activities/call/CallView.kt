package com.suhotrub.conversations.ui.activities.call

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import org.webrtc.MediaStream

interface CallView: MvpView{

    @StateStrategyType(SkipStrategy::class)
    fun onLocalMediaStream(mediaStream: MediaStream)

    @StateStrategyType(SkipStrategy::class)
    fun onRemoteMediaStream(mediaStream: MediaStream)
}