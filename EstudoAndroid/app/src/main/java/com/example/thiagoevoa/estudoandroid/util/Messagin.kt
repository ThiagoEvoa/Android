package com.example.thiagoevoa.estudoandroid.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.activity.NotificationActivity
import com.example.thiagoevoa.estudoandroid.activity.SplashActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


fun showNotification(context: Context, notificationManager: NotificationManager, notificationBuilder: NotificationCompat.Builder, title: String?, body: String?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.description
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.vibrationPattern = longArrayOf(0, 400, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
    }
    val activity = if (isUserLogged(context)) {
        NotificationActivity::class.java
    } else {
        SplashActivity::class.java
    }
    val intent = Intent(context, activity)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra(NOTIFICATION_TITLE, title)
            .putExtra(NOTIFICATION_BODY, body)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)
    notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setContentInfo(NOTIFICATION_CONTENT_INFO)
    notificationManager.notify(Random().nextInt(), notificationBuilder.build())
}

fun sendRegistrationTokenToServer(token: String?){
    Log.i("Token: ", token)
}

fun getFirebaseToken(): String?{
    var token: String? = null
    FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
        token = it.token
    }
    return token
}

fun registerToTopic(){
    FirebaseMessaging.getInstance().subscribeToTopic(DEFAULT_TOPIC)
}