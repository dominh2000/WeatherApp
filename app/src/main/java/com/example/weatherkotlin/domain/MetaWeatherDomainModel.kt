package com.example.weatherkotlin.domain

/*
    For MetaWeather API
 */
data class LocationInfo(
    val woeId: Int,
    val weatherSixDays: List<WeatherOneDay>,
    val currentTime: String,
    val sunRise: String,
    val sunSet: String,
    val timeZoneName: String,
    val title: String,
    val locationType: String,
    val lattLong: String,
    val timeZone: String
)

data class WeatherOneDay(
    val id: Long,
    val stateName: String,
    val stateAbbr: String,
    val windDirectionCompass: String,
    val timeStampCreated: String,
    val date: String,
    val minTemp: Double,
    val maxTemp: Double,
    val theTemp: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val airPressure: Double,
    val humidity: Int,
    val visibility: Double,
    val predictability: Int
)