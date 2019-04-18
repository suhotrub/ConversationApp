package com.suhotrub.conversations.ui.activities.call

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.suhotrub.conversations.base.di.scopes.ActivityScope
import com.suhotrub.conversations.interactor.signalr.MainHubInteractor
import com.suhotrub.conversations.ui.util.subscribe
import javax.inject.Inject


@InjectViewState
@ActivityScope
class CallPresenter @Inject constructor(
        private val mainHubInteractor: MainHubInteractor,
        private val context: Context
) : MvpPresenter<CallView>() {


    var webRTCWrapper: WebRTCWrapper? = null

    fun call() {
        webRTCWrapper = WebRTCWrapper(context, mainHubInteractor)
        observeMediaStreams()
        webRTCWrapper?.call()

    }
    fun stop() {
        webRTCWrapper?.stop()
        webRTCWrapper = null
    }
    private fun observeMediaStreams() {
        subscribe(webRTCWrapper?.observeLocalStream()) {
            viewState.onLocalMediaStream(it)
        }
        subscribe(webRTCWrapper?.observeRemoteStream()) {
            viewState.onRemoteMediaStream(it.first,it.second)
        }
        subscribe(webRTCWrapper?.observeIceConnectionStateChange()) {
            viewState.showMessage(it.toString())
        }
    }
}