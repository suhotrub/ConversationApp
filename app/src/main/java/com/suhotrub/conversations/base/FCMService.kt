package com.suhotrub.conversations.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.suhotrub.conversations.R
import com.suhotrub.conversations.model.messages.MessageDto
import com.suhotrub.conversations.model.webrtc.IncomingCallDto
import com.suhotrub.conversations.ui.activities.incomingcall.IncomingCallActivity
import com.suhotrub.conversations.ui.activities.main.MainActivity

/**
 * Сервис для работы с FCM
 */
class FCMService : FirebaseMessagingService() {

    /**
     * Вызывается при получении уведомления
     * @param p0 данные уведомления
     */
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

    /**
     * Показывает уведомление о сообщении
     * @param messageDto модель входящего сообщения
     */
    fun showMessagePush(messageDto: MessageDto) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager: NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("ConversationFCM", "ConversationsApp", importance)
            mChannel.description = "messaging"
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        } else {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        val notificationBuilder = NotificationCompat.Builder(this, "ConversationFCM")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_logo))
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle(messageDto.groupName ?: "Unknown")
                .setContentText(messageDto.text ?: "Empty message")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        notificationManager.notify(124653, notificationBuilder.build())
    }

    /**
     * Запускает активность входящего звонка
     */
    fun startIncomingCallActivity(incomingCallDto: IncomingCallDto) {
        startActivity(
                IncomingCallActivity
                        .prepareIntent(this, incomingCallDto)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

