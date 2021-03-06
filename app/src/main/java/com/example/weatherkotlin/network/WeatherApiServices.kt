package com.example.weatherkotlin.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.metaweather.com/api/location/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiServices {
    @GET("1236594")
    suspend fun getWeatherByLocation(): ResponseData
}

object WeatherApi {
    val retrofitServices: WeatherApiServices by lazy {
        retrofit.create(WeatherApiServices::class.java)
    }
}