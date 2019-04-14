package com.suhotrub.conversations.ui.activities.call

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.DragEvent
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.flexbox.FlexboxLayout
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.ui.util.ui.CustomDragShadow
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_call.*
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject


class CallActivity : MvpAppCompatActivity(), CallView, View.OnLongClickListener, View.OnDragListener {

    @Inject
    @InjectPresenter
    lateinit var presenter: CallPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onDestroy() {
        view1.release()
        listOf(flex,bottom,top).forEach {
            for (i in 0 until it.childCount)
                (it.getChildAt(i) as? SurfaceViewRenderer)?.release()
        }
        /*listOf(view1, view2, view3, view4, view5).forEach {
            it.release()
        }*/

        presenter.stop()
        super.onDestroy()
    }

    override fun onLocalMediaStream(mediaStream: MediaStream) {
        view1.setMirror(true)
        view1.init(presenter.webRTCWrapper.rootEglBase.eglBaseContext, null)
        view1.setZOrderMediaOverlay(true)

        mediaStream.videoTracks[0].addSink(view1)
    }

    val MAGIC_NUMBER = 1337
    override fun onRemoteMediaStream(mediaStream: MediaStream) {

        val isBottom = bottom.childCount == 0
        (if (isBottom) bottom else flex).addView(SurfaceViewRenderer(this@CallActivity).apply {

            val dp8 = dpToPx(8f).toInt()

            if (isBottom)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                    weight = 1F
                    setMargins(dp8, dp8, dp8, dp8)
                }
            else
                layoutParams = ViewGroup.MarginLayoutParams(dpToPx(100F).toInt(), dpToPx(100F).toInt()).apply {
                    setMargins(dp8, dp8, dp8, dp8)
                }

            setOnDragListener(this@CallActivity)
            setOnLongClickListener(this@CallActivity)
            init(presenter.webRTCWrapper.rootEglBase.eglBaseContext, null)
            setZOrderMediaOverlay(true)
            mediaStream.videoTracks[0].setEnabled(true)
            id = mediaStream.id.hashCode() + MAGIC_NUMBER
            mediaStream.videoTracks[0].addSink(this)

            this.invalidate()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_call)

        view1.setOnLongClickListener(this)
        listOf(flex, top, bottom).forEach { it.setOnDragListener(this) }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // request runtime permissions
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA,
                            Manifest.permission.CHANGE_NETWORK_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.WAKE_LOCK,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE),
                    100)
        }




        presenter.call()
        //SHOW VIDEO
        /*listOf(view1, view2, view3, view4, view5).forEach {
            val videoView = it
            videoView.init(rootEglBase.eglBaseContext, null)

            videoView.setMirror(true)
            localVideoTrack.addSink(videoView)

        }
*/
        var isFrontal = true
        /*videoView.setOnClickListener {
            isFrontal = !isFrontal

            val newLocalVideoTrack = initVideo(peerConnectionFactory, rootEglBase, isFrontal)

            videoView.setMirror(isFrontal)

            localVideoTrack.removeSink(videoView)
            localVideoTrack.setEnabled(false)
            localVideoTrack.dispose()

            newLocalVideoTrack.addSink(videoView)

            localVideoTrack = newLocalVideoTrack
        }*/
    }

    /**
     * WebRTC
     */

    /**
     * UI
     */

    override fun onLongClick(v: View): Boolean {
        if (v.parent == top && !(top.childCount >= bottom.childCount && bottom.childCount != 0))
            return true
        if (v.parent == bottom && !(bottom.childCount >= top.childCount && top.childCount != 0))
            return true

        vibrate()

        val data = ClipData.newPlainText("", "")
        val shadowBuilder = CustomDragShadow(v)
        ViewCompat.startDragAndDrop(v, data, shadowBuilder, v, 0)
        v.visibility = View.INVISIBLE
        return true

    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        if ((v.id == R.id.top || v.id == R.id.bottom) && (v as? ViewGroup)?.childCount == 2 && (event.localState as? View)?.parent != v)
            return false

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                val view = event.localState as? View

                if (top.childCount == 0 && v.id == R.id.bottom) {
                    if (event.y < v.y + v.height / 2)
                        top.visibility = View.VISIBLE
                } else if (bottom.childCount == 0 && v.id == R.id.top) {
                    if (event.y > v.y + v.height / 2)
                        bottom.visibility = View.VISIBLE
                } else {

                    when (v.id) {
                        R.id.flex -> {
                            val dp100 = dpToPx(100F).toInt()
                            FlexboxLayout.LayoutParams(dp100, dp100)
                        }
                        else -> {
                            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                                weight = 1F
                            }
                        }
                    }.apply {
                        val dp8 = dpToPx(8F).toInt()
                        setMargins(dp8, dp8, dp8, dp8)
                        view?.layoutParams = this
                    }
                }
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                //(v as? ViewGroup)?.removeView()
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                val view = event.localState as? View



                if (top.childCount == 0 && v.id == R.id.bottom) {
                    if (event.y < v.y + v.height / 2)
                        top.visibility = View.VISIBLE
                } else if (bottom.childCount == 0 && v.id == R.id.top) {
                    if (event.y > v.y + v.height / 2)
                        bottom.visibility = View.VISIBLE
                } else {

                    if (v.id == R.id.top && (bottom.childCount == 0 || (bottom.childCount == 1 && view?.parent == bottom)))
                        return true
                    if (v.id == R.id.bottom && (top.childCount == 0 || (top.childCount == 1 && view?.parent == top)))
                        return true


                    (view?.parent as? ViewGroup)?.removeView(view)

                    val viewGroup = (v as? ViewGroup)
                    val index =
                            viewGroup?.let {
                                for (i in 0 until viewGroup.childCount) {
                                    var childCenterx = viewGroup.getChildAt(i).x + viewGroup.getChildAt(0).width / 2
                                    if (event.x < childCenterx)
                                        return@let i
                                }
                                return@let viewGroup.childCount

                            } ?: 0

                    viewGroup?.addView(view, index)
                }
            }
            DragEvent.ACTION_DROP, DragEvent.ACTION_DRAG_ENDED -> {
                (event.localState as? View)?.visibility = View.VISIBLE

                if (top.childCount == 0) {
                    top.visibility = GONE
                }
                if (bottom.childCount == 0) {
                    top.getChildAt(0)?.let {
                        top.removeView(it)
                        bottom.addView(it)
                    }
                    top.visibility = GONE
                }
            }
            else -> {
            }
        }
        return true
    }

    private fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(25);
        }
    }

    fun dpToPx(dp: Float): Float {
        val density = this@CallActivity.resources.displayMetrics.density
        return dp * density
    }

    companion object {
        fun prepareIntent(ctx: Context, groupDto: GroupDto) =
                Intent(ctx, CallActivity::class.java).putExtra("EXTRA_FIRST", groupDto)
    }
}


data class Jsep(
        val type: String?,
        val sdp: String?
)

data class InitiateCallRequest(
        val offer: Jsep,
        val groupGuid: String
)

data class TrickleCandidateReceived(
        val sdpMid: String,
        val sdpMLineIndex: Int,
        val candidate: String,
        val completed: Boolean
)

data class TrickleResponse(
        val error: Int?,
        val data: String?
)

data class InititateCallResponse(
        val error: Int?,
        val data: Jsep
)
