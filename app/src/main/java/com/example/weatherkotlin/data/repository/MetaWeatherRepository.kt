package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.dataSources.database.WeatherDao
import com.example.weatherkotlin.data.dataSources.database.asDomainModel
import com.example.weatherkotlin.data.dataSources.network.WeatherApiServices
import com.example.weatherkotlin.data.dataSources.network.asDatabaseModel
import com.example.weatherkotlin.data.domainModel.LocationInfo
import com.example.weatherkotlin.data.domainModel.WeatherOneDay
import com.example.weatherkotlin.util.convert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MetaWeatherRepository @Inject constructor(
    val metaWeatherDao: WeatherDao,
    val apiServices: WeatherApiServices
) {

    val weatherByLocation: Flow<LocationInfo> =
        metaWeatherDao.getFullWeatherSixDays().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }

    val weatherToday: Flow<WeatherOneDay> = weatherByLocation.map {
        it.weatherSixDays.get(0)
    }

    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            val response = apiServices.getWeatherByLocation()
            val weatherSixDays = response.totalWeather.convert()
            metaWeatherDao.insertLocation(response.asDatabaseModel())
            metaWeatherDao.insertWeatherOneDay(weatherSixDays.asDatabaseModel(response))
        }
    }
}