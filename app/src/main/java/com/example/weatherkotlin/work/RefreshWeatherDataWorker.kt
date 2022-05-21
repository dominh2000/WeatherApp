package com.example.weatherkotlin.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.MainActivity
import com.example.weatherkotlin.R
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.repository.OpenWeatherRepository
import com.example.weatherkotlin.repository.WeatherRepository

class RefreshWeatherDataWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {

    companion object {
        const val WEATHER_WORK_NAME = "RefreshWeatherDataWorker"
    }

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val openWeatherDb = getDatabase(appContext)
        val openWeatherRepo = OpenWeatherRepository(openWeatherDb)

        try {
            openWeatherRepo.refreshOpenWeather()
        } catch (e: Exception) {
            Result.retry()
        }

        // ID tùy chọn
        val notificationId = 20

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(applicationContext, 0, intent, 0)

        val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_WEATHER_ID)
            .setSmallIcon(R.drawable.ic_noti)
            .setContentTitle("Thông báo")
            .setContentText("Đã cập nhật dữ liệu từ API Thời tiết")
            .setStyle(NotificationCompat.BigTextStyle().bigText("PeriodicWorkRequest sẽ tự động cập nhật cho bạn mỗi 3 giờ!"))
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }

        return Result.success()
    }

}