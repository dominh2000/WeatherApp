package com.example.weatherkotlin.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OpenWeatherDao {

    @Transaction
    @Query("SELECT * FROM city_for_forecast_5_days")
    fun getFullForecast5Days(): Flow<FullForecastFiveDays?>

    @Query("SELECT * FROM current_weather")
    fun getCurrentWeather(): Flow<CurrentWeather?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityForForecast5Days(cityForForecastFiveDays: CityForForecastFiveDays)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast5Days(forecast5Days: List<ForecastOneDay>)
}