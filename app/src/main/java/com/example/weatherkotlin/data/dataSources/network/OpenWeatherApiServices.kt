/**/package com.example.weatherkotlin.data.dataSources.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiServices {
    @GET("forecast?q=Hanoi,VN&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherForecast(): OpenWeatherForecastResponse

    @GET("weather?q=Hanoi,VN&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherCurrentWeather(): OpenWeatherCurrentWeatherResponse

    @GET("weather?&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherCurrentWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): OpenWeatherCurrentWeatherResponse

    @GET("forecast?&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherForecastByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): OpenWeatherForecastResponse
}