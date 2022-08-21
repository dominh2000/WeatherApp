package com.example.weatherkotlin.data.dataSources.network

import com.example.weatherkotlin.data.dataSources.database.DatabaseLocationInfo
import com.example.weatherkotlin.data.dataSources.database.DatabaseWeatherOneDay
import com.squareup.moshi.Json

data class ResponseData(
    @Json(name = "consolidated_weather") val totalWeather: List<TotalWeather>,
    @Json(name = "time") val currentTime: String,
    @Json(name = "sun_rise") val sunRise: String,
    @Json(name = "sun_set") val sunSet: String,
    @Json(name = "timezone_name") val timeZoneName: String,
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

// Extension functions
fun ResponseData.asDatabaseModel(): DatabaseLocationInfo {
    return this.let {
        DatabaseLocationInfo(
            it.woeId,
            it.currentTime,
            it.sunRise,
            it.sunSet,
            it.timeZoneName,
            it.title,
            it.locationType,
            it.lattLong,
            it.timeZone
        )
    }
}

fun List<TotalWeather>.asDatabaseModel(response: ResponseData): List<DatabaseWeatherOneDay> {
    val newListWithOrderedId = mutableListOf<TotalWeather>()

    // Sửa lại toàn bộ ID của danh sách để thêm vào DB đúng thứ tự
    for (it in this) {
        newListWithOrderedId.add(it.copy(id = this.indexOf(it).toLong() + 1))
    }
    return newListWithOrderedId.map {
        DatabaseWeatherOneDay(
            it.id,
            response.woeId,
            it.stateName,
            it.stateAbbr,
            it.windDirectionCompass,
            it.timeStampCreated,
            it.date,
            it.minTemp,
            it.maxTemp,
            it.theTemp,
            it.windSpeed,
            it.windDirection,
            it.airPressure,
            it.humidity,
            it.visibility,
            it.predictability
        )
    }
}