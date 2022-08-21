package com.example.weatherkotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import com.example.weatherkotlin.data.dataSources.database.ApplicationRoomDatabase
import com.example.weatherkotlin.data.repository.MetaWeatherRepository
import com.example.weatherkotlin.data.repository.OpenWeatherRepository
import com.example.weatherkotlin.data.repository.ToDoRepository
import com.example.weatherkotlin.ui.work.RefreshWeatherDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class BaseApplication : Application() {

    // CoroutineScope with Default Dispatchers to launch delayed Init
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    // Lazy initialization for singletons throughout the app
    val databaseApplication: ApplicationRoomDatabase by lazy {
        ApplicationRoomDatabase.getDatabase(this, "Hello123!")
    }
    val openWeatherRepository: OpenWeatherRepository by lazy {
        OpenWeatherRepository(databaseApplication)
    }
    val toDoRepository: ToDoRepository by lazy {
        ToDoRepository(databaseApplication)
    }
    val metaWeatherRepository: MetaWeatherRepository by lazy {
        MetaWeatherRepository(databaseApplication)
    }

    companion object {
        const val CHANNEL_WEATHER_ID = "weather_update_id"
        const val CHANNEL_TASK_ID = "task_id"
    }

    override fun onCreate() {
        super.onCreate()
        delayedInit()
//        DynamicColors.applyToActivitiesIfAvailable(this);
    }

    private fun delayedInit() {
        applicationScope.launch {
            createNotificationChannelWithImportanceHigh(
                CHANNEL_WEATHER_ID,
                "Thời tiết",
                "Thông báo cập nhật thời tiết"
            )
            createNotificationChannelWithImportanceHigh(
                CHANNEL_TASK_ID,
                "Nhắc việc",
                "Nhắc các công việc đã đặt chuông báo"
            )
            refreshWeatherData()
        }
    }

    private fun createNotificationChannelWithImportanceHigh(
        channelId: String,
        channelName: String,
        channelDescription: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun refreshWeatherData() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val weatherWorkRequest =
            PeriodicWorkRequestBuilder<RefreshWeatherDataWorker>(3, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            RefreshWeatherDataWorker.WEATHER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            weatherWorkRequest
        )
    }
}