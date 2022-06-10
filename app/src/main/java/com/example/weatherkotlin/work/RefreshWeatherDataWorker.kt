package com.example.weatherkotlin.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.repository.OpenWeatherRepository
import com.example.weatherkotlin.util.sendNotificationWithContentIntent

class RefreshWeatherDataWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    companion object {
        const val WEATHER_WORK_NAME = "RefreshWeatherDataWorker"
    }

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val openWeatherDb = getDatabase(appContext)
        val openWeatherRepo = OpenWeatherRepository(openWeatherDb)

        try {
            openWeatherRepo.refreshOpenWeather()

            val notificationId = 10000
            val contentTitle = "Thời tiết"
            val contentText = "Đã cập nhật thông tin thời tiết"
            val bigText =
                "Dữ liệu từ OpenWeather API sẽ được tự động cập nhật mỗi 3 giờ với WorkManager!"

            val notificationManager = ContextCompat.getSystemService(
                appContext,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotificationWithContentIntent(
                notificationId,
                BaseApplication.CHANNEL_WEATHER_ID,
                R.drawable.ic_noti,
                contentTitle,
                contentText,
                bigText,
                appContext
            )

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

}