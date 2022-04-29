package com.example.weatherkotlin.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Transaction
    @Query("SELECT * FROM weather_response WHERE woe_id = 1236594")
    fun getFullWeatherSixDays(): Flow<DatabaseFullWeather?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationInfo: DatabaseLocationInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherOneDay(weatherOneDay: List<DatabaseWeatherOneDay>)
}