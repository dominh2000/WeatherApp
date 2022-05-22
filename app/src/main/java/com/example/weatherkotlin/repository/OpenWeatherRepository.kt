package com.example.weatherkotlin.repository

import com.example.weatherkotlin.database.ApplicationRoomDatabase
import com.example.weatherkotlin.database.asDomainModel
import com.example.weatherkotlin.domain.OpenWeatherCurrentWeather
import com.example.weatherkotlin.domain.OpenWeatherForecastFiveDays
import com.example.weatherkotlin.network.OpenWeatherApi
import com.example.weatherkotlin.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class OpenWeatherRepository(private val database: ApplicationRoomDatabase) {

    val currentWeather: Flow<OpenWeatherCurrentWeather> =
        database.openWeatherDao().getCurrentWeather().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }

    val fiveDaysForecast: Flow<OpenWeatherForecastFiveDays> =
        database.openWeatherDao().getFullForecast5Days().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }

    suspend fun refreshOpenWeather() {
        withContext(Dispatchers.IO) {
            val fiveDaysForecast = OpenWeatherApi.retrofitServices.getOpenWeatherForecast()
            val currentWeather = OpenWeatherApi.retrofitServices.getOpenWeatherCurrentWeather()
            database.openWeatherDao().insertCityForForecast5Days(fiveDaysForecast.asDatabaseModel())
            database.openWeatherDao()
                .insertForecast5Days(fiveDaysForecast.listForecast.asDatabaseModel())
            database.openWeatherDao().insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }

    suspend fun getOpenWeatherByCoord(latt: Double, long: Double) {
        withContext(Dispatchers.IO) {
            val currentWeather =
                OpenWeatherApi.retrofitServices.getOpenWeatherCurrentWeatherByCoord(latt, long)
            val fiveDaysForecast =
                OpenWeatherApi.retrofitServices.getOpenWeatherForecastByCoord(latt, long)
            database.openWeatherDao().insertCityForForecast5Days(fiveDaysForecast.asDatabaseModel())
            database.openWeatherDao()
                .insertForecast5Days(fiveDaysForecast.listForecast.asDatabaseModel())
            database.openWeatherDao().insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }
}