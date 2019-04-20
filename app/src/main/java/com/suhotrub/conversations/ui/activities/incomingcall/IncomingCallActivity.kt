package com.suhotrub.conversations.ui.activities.incomingcall

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.webrtc.IncomingCallDto
import com.suhotrub.conversations.ui.activities.call.CallActivity
import com.suhotrub.conversations.ui.util.ui.setTextOrGone
import kotlinx.android.synthetic.main.activity_incoming_call.*


class IncomingCallActivity : AppCompatActivity() {

    val r by lazy {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_incoming_call)

        StatusBarUtil.setTransparent(this)
        r.play()

        intent.getParcelableExtra<IncomingCallDto>("EXTRA_FIRST")?.let { incomingCallDto ->
            textView4.setTextOrGone(incomingCallDto.group.name)

            imageButton2.setOnClickListener {
                startActivity(CallActivity.prepareIntent(this@IncomingCallActivity, incomingCallDto.group))
                finish()
            }
        }

        imageButton3.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        r.stop()
        super.onDestroy()
    }
    companion object {
        fun prepareIntent(ctx: Context, incomingCallDto: IncomingCallDto) =
                Intent(ctx, IncomingCallActivity::class.java).putExtra("EXTRA_FIRST", incomingCallDto)
    }
}