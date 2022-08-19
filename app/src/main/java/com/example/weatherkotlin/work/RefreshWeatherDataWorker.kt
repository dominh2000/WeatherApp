package com.example.weatherkotlin.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.datastore.WeatherLocationDataStore
import com.example.weatherkotlin.repository.OpenWeatherRepository
import com.example.weatherkotlin.util.sendNotificationWithContentIntent
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt

class RefreshWeatherDataWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    companion object {
        const val WEATHER_WORK_NAME = "RefreshWeatherDataWorker"
    }

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val weatherLocationDataStore = WeatherLocationDataStore(appContext)
        val openWeatherDb = getDatabase(appContext, "Hello123!")
        val openWeatherRepo = OpenWeatherRepository(openWeatherDb)

        return try {
            weatherLocationDataStore.locationFlow.first().let {
                if (it[0] == 0.0 && it[1] == 0.0) {
                    openWeatherRepo.refreshOpenWeather()
                } else {
                    openWeatherRepo.getOpenWeatherByCoord(it[0], it[1])
                }
            }

            openWeatherRepo.currentWeather.first().let {
                val notificationId = 10000
                val contentTitle = "Thời tiết ".plus(it.cityName)
                val contentText = it.forecastInfo.temp.roundToInt().toString().plus("°C | ")
                    .plus(
                        "Cảm giác như ".plus(
                            it.forecastInfo.feelsLike.roundToInt().toString().plus("°C | ")
                        )
                    )
                    .plus(
                        it.currentWeatherDescription.description[0].uppercase()
                            .plus(it.currentWeatherDescription.description.substring(1))
                    )
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
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

}