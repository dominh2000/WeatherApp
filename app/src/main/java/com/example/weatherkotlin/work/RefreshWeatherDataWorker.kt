package com.example.weatherkotlin.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.MainActivity
import com.example.weatherkotlin.R
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.repository.OpenWeatherRepository

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

            val notificationId = 1000
            val contentIntent = Intent(applicationContext, MainActivity::class.java)
            // Support Android 12+ and compatible with lower versions
            val flags = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else -> PendingIntent.FLAG_UPDATE_CURRENT
            }
            val contentPendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                contentIntent,
                flags
            )

            val builder =
                NotificationCompat.Builder(
                    applicationContext,
                    BaseApplication.CHANNEL_WEATHER_ID
                )
                    .setSmallIcon(R.drawable.ic_noti)
                    .setContentTitle("Thời tiết")
                    .setContentText("Đã cập nhật thông tin thời tiết")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Dữ liệu từ OpenWeather API sẽ được tự động cập nhật mỗi 3 giờ với WorkManager!")
                    )
                    .setColor(Color.GREEN)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(contentPendingIntent)
                    .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }

}