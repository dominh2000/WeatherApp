package com.example.weatherkotlin.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.weatherkotlin.MainActivity
import com.example.weatherkotlin.R

fun NotificationManager.sendNotificationWithContentIntent(
    notificationId: Int,
    notificationChannel: String,
    messageTitle: String,
    messageBody: String,
    ctx: Context
) {
    val contentIntent = Intent(ctx, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        ctx,
        notificationId,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(ctx, notificationChannel)
        .setSmallIcon(R.drawable.ic_to_do)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setColor(Color.BLUE)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(notificationId, builder.build())
}