package com.example.weatherkotlin.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OpenWeatherApiServices {
    @GET("forecast?q=Hanoi,VN&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherForecast(): OpenWeatherForecastResponse

    @GET("weather?q=Hanoi,VN&appid=890750f32b17372e6e02043159756cc6&units=metric&lang=vi")
    suspend fun getOpenWeatherCurrentWeather(): OpenWeatherCurrentWeatherResponse
}

object OpenWeatherApi {
    val retrofitServices: OpenWeatherApiServices by lazy {
        retrofit.create(OpenWeatherApiServices::class.java)
    }
}