package com.example.weatherkotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import com.example.weatherkotlin.database.ApplicationRoomDatabase
import com.example.weatherkotlin.work.RefreshWeatherDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BaseApplication: Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        const val CHANNEL_ID = "weather_update_id"
    }

    override fun onCreate() {
        super.onCreate()
        delayedInit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "weather_channel"
            val descriptionText = "weather_update_reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun delayedInit() {
        applicationScope.launch {
            refreshWeatherData()
        }
    }

    val databaseApplication: ApplicationRoomDatabase by lazy {
        ApplicationRoomDatabase.getDatabase(this)
    }

    private fun refreshWeatherData() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val weatherWorkRequest = PeriodicWorkRequestBuilder<RefreshWeatherDataWorker>(3, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshWeatherDataWorker.WEATHER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            weatherWorkRequest
        )
    }
}