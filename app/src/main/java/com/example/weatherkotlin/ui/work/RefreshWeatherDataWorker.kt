package com.example.weatherkotlin.ui.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.dataSources.datastore.WeatherLocationDataStore
import com.example.weatherkotlin.data.repository.OpenWeatherRepository
import com.example.weatherkotlin.util.sendNotificationWithContentIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt

@HiltWorker
class RefreshWeatherDataWorker @AssistedInject constructor(
    @Assisted val ctx: Context,
    @Assisted val params: WorkerParameters,
    val repository: OpenWeatherRepository
) :
    CoroutineWorker(ctx, params) {

    companion object {
        const val WEATHER_WORK_NAME = "RefreshWeatherDataWorker"
    }

    override suspend fun doWork(): Result {
        val appContext = applicationContext
        val weatherLocationDataStore = WeatherLocationDataStore(appContext)

        return try {
            weatherLocationDataStore.locationFlow.first().let {
                if (it[0] == 0.0 && it[1] == 0.0) {
                    repository.refreshOpenWeather()
                } else {
                    repository.getOpenWeatherByCoord(it[0], it[1])
                }
            }

            repository.currentWeather.first().let {
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
                val bigText = contentText.plus("\n").plus(
                    "Dữ liệu từ OpenWeather API sẽ được tự động cập nhật mỗi 3 giờ với WorkManager!"
                )

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