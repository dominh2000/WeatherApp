package com.example.weatherkotlin.data.dataSources.network

import retrofit2.http.GET

interface WeatherApiServices {
    @GET("1236594")
    suspend fun getWeatherByLocation(): ResponseData
}