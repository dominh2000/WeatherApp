package com.example.weatherkotlin.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.weatherkotlin.MainActivity
import com.example.weatherkotlin.R

fun NotificationManager.sendNotificationWithContentIntent(
    notificationId: Int,
    notificationChannel: String,
    smallIcon: Int,
    messageTitle: String,
    messageBody: String,
    bigText: String,
    ctx: Context
) {
    val contentIntent = Intent(ctx, MainActivity::class.java)
    // Support Android 12+ and compatible with lower versions
    val flags = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        else -> PendingIntent.FLAG_UPDATE_CURRENT
    }
    val contentPendingIntent = PendingIntent.getActivity(
        ctx,
        notificationId,
        contentIntent,
        flags
    )

    val builder = NotificationCompat.Builder(ctx, notificationChannel)
        .setSmallIcon(smallIcon)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setColor(Color.GREEN)
        .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(notificationId, builder.build())
}

fun NotificationManager.sendNotificationWithDeepLink(
    notificationId: Int,
    notificationChannel: String,
    messageTitle: String,
    messageBody: String,
    ctx: Context
) {
    val pendingIntentFromDeepLink = NavDeepLinkBuilder(ctx)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.fragmentToDoStart)
        .createPendingIntent()

    val builder = NotificationCompat.Builder(ctx, notificationChannel)
        .setSmallIcon(R.drawable.ic_to_do)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setColor(Color.GREEN)
        .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntentFromDeepLink)
        .setAutoCancel(true)

    notify(notificationId, builder.build())
}