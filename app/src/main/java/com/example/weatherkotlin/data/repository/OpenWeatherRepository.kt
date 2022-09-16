package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.dataSources.database.OpenWeatherDao
import com.example.weatherkotlin.data.dataSources.database.asDomainModel
import com.example.weatherkotlin.data.dataSources.network.OpenWeatherApiServices
import com.example.weatherkotlin.data.dataSources.network.asDatabaseModel
import com.example.weatherkotlin.data.domainModel.OpenWeatherCurrentWeather
import com.example.weatherkotlin.data.domainModel.OpenWeatherForecastFiveDays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(
    val openWeatherDao: OpenWeatherDao,
    val apiServices: OpenWeatherApiServices
) {

    val currentWeather: Flow<OpenWeatherCurrentWeather> =
        openWeatherDao.getCurrentWeather().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }

    val fiveDaysForecast: Flow<OpenWeatherForecastFiveDays> =
        openWeatherDao.getFullForecast5Days().transform {
            if (it != null) {
                emit(it.asDomainModel())
            }
        }

    suspend fun refreshOpenWeather() {
        withContext(Dispatchers.IO) {
            val fiveDaysForecast = apiServices.getOpenWeatherForecast()
            val currentWeather = apiServices.getOpenWeatherCurrentWeather()
            openWeatherDao.insertCityForForecast5Days(fiveDaysForecast.asDatabaseModel())
            openWeatherDao
                .insertForecast5Days(fiveDaysForecast.listForecast.asDatabaseModel())
            openWeatherDao.insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }

    suspend fun getOpenWeatherByCoord(latt: Double, long: Double) {
        withContext(Dispatchers.IO) {
            val currentWeather =
                apiServices.getOpenWeatherCurrentWeatherByCoord(latt, long)
            val fiveDaysForecast =
                apiServices.getOpenWeatherForecastByCoord(latt, long)
            openWeatherDao.insertCityForForecast5Days(fiveDaysForecast.asDatabaseModel())
            openWeatherDao
                .insertForecast5Days(fiveDaysForecast.listForecast.asDatabaseModel())
            openWeatherDao.insertCurrentWeather(currentWeather.asDatabaseModel())
        }
    }
}