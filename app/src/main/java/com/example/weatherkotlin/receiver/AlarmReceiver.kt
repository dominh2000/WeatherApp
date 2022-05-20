package com.example.weatherkotlin.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.weatherkotlin.util.sendNotificationWithContentIntent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notiId", 0)
        val notificationChannel = intent.getStringExtra("notiChannel")
        val messageTitle = intent.getStringExtra("title")
        val messageBody = intent.getStringExtra("body")

        // TODO: For testing
        // println("$notificationId $notificationChannel $messageTitle $messageBody")

        val notificationManager = ContextCompat.getSystemService(
            ctx,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotificationWithContentIntent(
            notificationId,
            notificationChannel!!,
            messageTitle!!,
            messageBody!!,
            ctx
        )
    }
}