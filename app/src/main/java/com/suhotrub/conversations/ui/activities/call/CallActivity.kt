package com.suhotrub.conversations.ui.activities.call

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.flexbox.FlexboxLayout
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.user.UserDto
import com.suhotrub.conversations.ui.activities.call.ui.PublisherView
import com.suhotrub.conversations.ui.util.ui.showError
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_call.*
import org.webrtc.MediaStream
import org.webrtc.SurfaceViewRenderer
import javax.inject.Inject


class CallActivity : MvpAppCompatActivity(), CallView, View.OnClickListener {

    @Inject
    @InjectPresenter
    lateinit var presenter: CallPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onDestroy() {
        try {
            audioManager.isSpeakerphoneOn = isSpeakerPhoneOn;
            audioManager.mode = oldAudioMode;
        } catch (t: Throwable) {
        }

        listOf(flex, video_container).forEach {
            for (i in 0 until it.childCount) {
                (it.getChildAt(i) as? SurfaceViewRenderer)?.release()
                (it.getChildAt(i) as? PublisherView)?.release()
            }
        }
        presenter.stop()
        super.onDestroy()
    }

    override fun onLocalMediaStream(mediaStream: MediaStream) {
        view1.setMirror(true)
        view1.init(presenter.webRTCWrapper?.rootEglBase?.eglBaseContext, null)
        view1.setZOrderMediaOverlay(true)

        mediaStream.videoTracks[0].addSink(view1)
    }

    override fun onRemoteStreamUnpublished(handleId: Long) {
        listOf(flex, video_container).forEach {
            for (i in 0 until it.childCount) {
                if ((it.getChildAt(i) as? PublisherView)?.userDto?.handleId == handleId) {
                    (it.getChildAt(i) as? PublisherView)?.release()
                    ((it.getChildAt(i) as? PublisherView)?.parent as? ViewGroup)?.removeViewAt(i)
                    return
                }
            }
        }
    }

    override fun onRemoteMediaStream(mediaStream: MediaStream, userDto: UserDto?) {

        val isBottom = video_container.childCount == 0
        val view = PublisherView(this@CallActivity).apply {

            if (isBottom)
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            else
                layoutParams = FlexboxLayout.LayoutParams(dpToPx(100F).toInt(), dpToPx(100F).toInt())


            setOnClickListener(this@CallActivity)
            invalidate()
        }

        (if (isBottom) video_container else flex).addView(view)
        view.postDelayed({
            view.setStream(presenter.webRTCWrapper?.rootEglBase?.eglBaseContext, mediaStream, userDto
                    ?: UserDto())
            view.invalidate()
        }, 1000)

    }

    override fun onClick(v: View) {
        if (v.parent != video_container) {
            var position = 0

            (v.parent as ViewGroup).let {
                for (i in 0 until it.childCount) {
                    if (it.getChildAt(i) == v)
                        position = i
                }
            }
            flex.removeViewAt(position)

            if (video_container.childCount != 0) {
                val bigView = video_container.getChildAt(0)
                video_container.removeViewAt(0)
                flex.addView(bigView, position)
                bigView.layoutParams = FlexboxLayout.LayoutParams(dpToPx(100F).toInt(), dpToPx(100F).toInt()).apply {
                }
            }
            v.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            }
            video_container.addView(v)
        }
    }

    private var oldAudioMode: Int = 0
    private var isSpeakerPhoneOn: Boolean = true

    private lateinit var audioManager: AudioManager
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        try {
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

            oldAudioMode = audioManager.mode;
            isSpeakerPhoneOn = audioManager.isSpeakerphoneOn;

            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.isSpeakerphoneOn = true
        } catch (t: Throwable) {
        }

        setContentView(R.layout.activity_call)

        StatusBarUtil.setTransparent(this)

        view1.setOnClickListener(this)

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
        } else {
            presenter.call()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            grantResults.find { it != PackageManager.PERMISSION_GRANTED }?.let {
                finish()
                return
            }
            presenter.call()
        }
    }

    override fun showMessage(text: String) {
        showError(text)
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