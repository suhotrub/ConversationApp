package com.suhotrub.conversations.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.group.GroupDto
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.ui.activities.call.CallActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity
import com.suhotrub.conversations.ui.activities.splash.SplashActivity




class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        try {
            if (p0?.data.isNullOrEmpty())
                super.onMessageReceived(p0)
            else
                p0?.data?.let {
                    when (it["type"]) {
                        "CallStarted" -> {
                            startIncomingCallActivity(
                                    Gson().fromJson(
                                            it["data"],
                                            IncomingCallDto::class.java
                                    )
                            )
                        }
                        "IncomingMessage" -> {
                            showMessagePush(
                                    Gson().fromJson(
                                            it["data"],
                                            MessageDto::class.java
                                    )
                            )
                        }
                    }
                }
        } catch (ignored: Throwable) {
            return super.onMessageReceived(p0)
        }
    }

    fun showMessagePush(messageDto: MessageDto) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_logo))
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(messageDto.groupName)
                .setContentText(messageDto.text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    fun startIncomingCallActivity(incomingCallDto: IncomingCallDto) {
        startActivity(Intent(this, CallActivity::class.java))
    }
}

data class IncomingCallDto(val group: GroupDto)