package com.example.weatherkotlin.data.dataSources.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiServices {
    @GET("forecast?q=Hanoi,VN&units=metric&lang=vi")
    suspend fun getOpenWeatherForecast(): OpenWeatherForecastResponse

    @GET("weather?q=Hanoi,VN&units=metric&lang=vi")
    suspend fun getOpenWeatherCurrentWeather(): OpenWeatherCurrentWeatherResponse

    @GET("weather?&units=metric&lang=vi")
    suspend fun getOpenWeatherCurrentWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): OpenWeatherCurrentWeatherResponse

    @GET("forecast?&units=metric&lang=vi")
    suspend fun getOpenWeatherForecastByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): OpenWeatherForecastResponse
}