package com.suhotrub.conversations.ui.activities.call

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.support.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
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
    }

    fun release() {
        surface_view.release()
        stream?.dispose()
    }

    var stream: MediaStream? = null
    var userDto: UserDto? = null

    fun setStream(eglseBaseContext: EglBase.Context?, mediaStream: MediaStream, userDto: UserDto) {
        stream = mediaStream
        this.userDto = userDto

        surface_view.init(eglseBaseContext, object : RendererCommon.RendererEvents {
            override fun onFirstFrameRendered() {
                post {
                    publisher_progress.setVisibleOrGone(false)
                }
            }

            override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) {
            }
        })
        surface_view.setZOrderMediaOverlay(true)
        mediaStream.videoTracks[0].setEnabled(true)
        mediaStream.videoTracks[0].addSink(surface_view)


        username.text = userDto.login
        makeInfoVisible()

        mute.setOnClickListener {
            if (mediaStream.audioTracks[0].enabled()) {
                mute.setImageResource(R.drawable.ic_volume_off_white_24dp)
                mediaStream.audioTracks[0].setEnabled(false)
            } else {
                mute.setImageResource(R.drawable.ic_volume_up_white_24dp)
                mediaStream.audioTracks[0].setEnabled(true)
            }
        }
    }

    private fun changeAlpha(view: View, to: Float){
        ObjectAnimator.ofFloat(view, "alpha", view.alpha, to)
                .setDuration(400)
                .start()
    }

    var showed = false
    fun makeInfoVisible() {
        showed = true

        changeAlpha(username,1F)
        changeAlpha(bottom_panel,1F)

        postDelayed({
            makeInvisible()

        }, 2000)
    }

    fun makeInvisible() {
        showed = false

        changeAlpha(username,0F)
        changeAlpha(bottom_panel,0F)
    }

    override fun performClick(): Boolean {
        if (showed)
            return super.performClick()
        else
            makeInfoVisible()

        return true
    }
}