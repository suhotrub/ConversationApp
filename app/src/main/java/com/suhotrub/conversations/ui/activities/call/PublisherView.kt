package com.suhotrub.conversations.ui.activities.call

import android.content.Context
import android.graphics.Canvas
import android.support.transition.TransitionManager
import android.util.AttributeSet
import android.widget.FrameLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.util.ui.setVisibleOrGone
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.view_publisher.view.*
import org.webrtc.EglBase
import org.webrtc.MediaStream
import org.webrtc.RendererCommon
import java.util.*

class PublisherView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {


    init {
        inflate(context, R.layout.view_publisher, this)
        surface_view.setZOrderMediaOverlay(true)
        setWillNotDraw(false)
    }

    fun release() {
        surface_view.release()
        stream?.dispose()
    }

    var stream: MediaStream? = null

    fun setStream(eglseBaseContext: EglBase.Context?, mediaStream: MediaStream, userDto: UserDto) {
        stream = mediaStream

        username.text = userDto.login

        surface_view.init(eglseBaseContext, object : RendererCommon.RendererEvents {
            override fun onFirstFrameRendered() {
                progressBar.setVisibleOrGone(false)

            }

            override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) {
            }
        })

        mute.setOnClickListener {
            if (mediaStream.audioTracks[0].enabled()) {
                mute.setImageResource(R.drawable.ic_volume_off_white_24dp)
                mediaStream.audioTracks[0].setEnabled(false)
            } else {
                mute.setImageResource(R.drawable.ic_volume_up_white_24dp)
                mediaStream.audioTracks[0].setEnabled(true)
            }
        }

        mediaStream.videoTracks[0].setEnabled(true)
        mediaStream.videoTracks[0].addSink(surface_view)

        makeInfoVisible()
    }

    var showed = false
    fun makeInfoVisible() {
        showed = true
        TransitionManager.beginDelayedTransition(this)
        username.alpha = 1F
        bottom_panel.alpha = 1F
        postDelayed({
            makeInvisible()

        },2000)
    }

    fun makeInvisible() {
        showed = false
        TransitionManager.beginDelayedTransition(this)
        username.alpha = 0F
        bottom_panel.alpha = 0F
    }

    override fun performClick(): Boolean {
        if (showed)
            return super.performClick()
        else
            makeInfoVisible()

        return true
    }
}