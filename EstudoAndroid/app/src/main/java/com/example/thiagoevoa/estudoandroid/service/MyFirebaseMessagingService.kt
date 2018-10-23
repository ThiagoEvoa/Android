package com.example.thiagoevoa.estudoandroid.service

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.example.thiagoevoa.estudoandroid.util.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    var title: String? = null
    var body: String? = null

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        sendRegistrationTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        if(remoteMessage.data.isEmpty()){
            title = remoteMessage.notification?.title
            body = remoteMessage.notification?.body
        }else{
            title = remoteMessage.data[NOTIFICATION_TITLE]
            body = remoteMessage.data[NOTIFICATION_BODY]
        }
        showNotification(this, notificationManager, notificationBuilder, title, body)
    }
}