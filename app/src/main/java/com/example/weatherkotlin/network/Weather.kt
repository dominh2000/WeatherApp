package com.example.weatherkotlin.network

import com.squareup.moshi.Json

data class ResponseData(
    @Json(name = "consolidated_weather") val totalWeather: List<TotalWeather>,
    @Json(name = "time") val currentTime: String,
    @Json(name = "sun_rise") val sunRise: String,
    @Json(name = "sun_set") val sunSet: String,
    @Json(name = "timezone_name") val timezone: String,
    @Json(name = "parent") val parent: Parent,
    @Json(name = "sources") val sources: List<Source>,
    @Json(name = "title") val title: String,
    @Json(name = "location_type") val locationType: String,
    @Json(name = "woeid") val woeId: Int,
    @Json(name = "latt_long") val lattLong: String,
    @Json(name = "timezone") val timeZone: String
)

data class TotalWeather(
    @Json(name = "id") val id: Long,
    @Json(name = "weather_state_name") val stateName: String,
    @Json(name = "weather_state_abbr") val stateAbbr: String,
    @Json(name = "wind_direction_compass") val windDirectionCompass: String,
    @Json(name = "created") val timeStampCreated: String,
    @Json(name = "applicable_date") val date: String,
    @Json(name = "min_temp") val minTemp: Double,
    @Json(name = "max_temp") val maxTemp: Double,
    @Json(name = "the_temp") val theTemp: Double,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_direction") val windDirection: Double,
    @Json(name = "air_pressure") val airPressure: Double,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "visibility") val visibility: Double,
    @Json(name = "predictability") val predictability: Int
)

data class Parent(
    @Json(name = "title") val title: String,
    @Json(name = "location_type") val locationType: String,
    @Json(name = "woeid") val woeId: Int,
    @Json(name = "latt_long") val lattLong: String
)

data class Source(
    @Json(name = "title") val title: String,
    @Json(name = "slug") val slug: String,
    @Json(name = "url") val url: String,
    @Json(name = "crawl_rate") val crawlRate: Int,
)