package com.example.weatherkotlin.repository

import com.example.weatherkotlin.database.ApplicationRoomDatabase
import com.example.weatherkotlin.database.asDomainModel
import com.example.weatherkotlin.domain.LocationInfo
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.network.WeatherApi
import com.example.weatherkotlin.network.asDatabaseModel
import com.example.weatherkotlin.util.convert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class MetaWeatherRepository(private val database: ApplicationRoomDatabase) {

    val weatherByLocation: Flow<LocationInfo> =
        database.weatherDao().getFullWeatherSixDays().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }
    val weatherToday: Flow<WeatherOneDay> = weatherByLocation.map {
        it.weatherSixDays.get(0)
    }

    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            val response = WeatherApi.retrofitServices.getWeatherByLocation()
            val weatherSixDays = response.totalWeather.convert()
            database.weatherDao().insertLocation(response.asDatabaseModel())
            database.weatherDao().insertWeatherOneDay(weatherSixDays.asDatabaseModel(response))
        }
    }
}