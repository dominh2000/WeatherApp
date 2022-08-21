package com.example.weatherkotlin.data.dataSources.network

import com.example.weatherkotlin.data.dataSources.database.CityForForecastFiveDays
import com.example.weatherkotlin.data.dataSources.database.CurrentWeather
import com.example.weatherkotlin.data.dataSources.database.ForecastOneDay
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenWeatherCurrentWeatherResponse(
    @Json(name = "coord") val coordinate: Coordinate,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "base") val base: String,
    @Json(name = "main") val mainInfo: MainCurrentWeatherInfo,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "clouds") val clouds: Clouds,
    @Json(name = "rain") val rain: RainForCurrentWeather? = null,
    @Json(name = "dt") val dateTimeInt: Int,
    @Json(name = "sys") val systemInfo: SystemInfoCurrentWeather,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "id") val cityId: Int,
    @Json(name = "name") val cityName: String,
    @Json(name = "cod") val cod: Int
)

data class OpenWeatherForecastResponse(
    @Json(name = "cod") val cod: String,
    @Json(name = "message") val message: Int,
    @Json(name = "cnt") val cnt: Int,
    @Json(name = "list") val listForecast: List<Forecast>,
    @Json(name = "city") val city: City
)

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "dt") val dateTimeInt: Int,
    @Json(name = "main") val mainInfo: MainForecastInfo,
    @Json(name = "weather") val weatherDesc: List<WeatherDescription>,
    @Json(name = "clouds") val clouds: Clouds,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "pop") val precipitationProbability: Double,
    @Json(name = "rain") val rain: Rain? = null,
    @Json(name = "sys") val systemInfo: SystemInfo,
    @Json(name = "dt_txt") val dateTimeText: String
)

data class MainForecastInfo(
    @Json(name = "temp") val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val minTemp: Double,
    @Json(name = "temp_max") val maxTemp: Double,
    @Json(name = "pressure") val defaultPressure: Int,
    @Json(name = "sea_level") val seaLevelPressure: Int,
    @Json(name = "grnd_level") val groundLevelPressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "temp_kf") val tempKf: Double
)

data class MainCurrentWeatherInfo(
    @Json(name = "temp") val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val minTemp: Double,
    @Json(name = "temp_max") val maxTemp: Double,
    @Json(name = "pressure") val defaultPressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "sea_level") val seaLevelPressure: Int,
    @Json(name = "grnd_level") val groundLevelPressure: Int,
)

data class WeatherDescription(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val mainWeatherType: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)

data class SystemInfo(
    @Json(name = "pod") val pod: String
)

data class SystemInfoCurrentWeather(
    @Json(name = "country") val country: String,
    @Json(name = "sunrise") val sunrise: Int,
    @Json(name = "sunset") val sunset: Int,
)

data class City(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "coord") val coordinate: Coordinate,
    @Json(name = "country") val country: String,
    @Json(name = "population") val population: Int,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "sunrise") val sunrise: Int,
    @Json(name = "sunset") val sunset: Int
)

data class Coordinate(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double
)

data class Clouds(
    @Json(name = "all") val percent: Int
)

data class Wind(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val degree: Int,
    @Json(name = "gust") val gust: Double
)

data class Rain(
    @Json(name = "3h") val last3hVolume: Double
)

@JsonClass(generateAdapter = true)
data class RainForCurrentWeather(
    @Json(name = "1h") val last1hVolume: Double,
    @Json(name = "3h") val last3hVolume: Double? = null,
)

// Extension functions
fun OpenWeatherForecastResponse.asDatabaseModel(): CityForForecastFiveDays {
    return this.let {
        CityForForecastFiveDays(
            1,
            it.city.name
        )
    }
}

fun List<Forecast>.asDatabaseModel(): List<ForecastOneDay> {
    val newListForecastWithChangedId = mutableListOf<Forecast>()

    // Sửa lại toàn bộ dateTimeInt của list thành ID tự động để thuận tiện cho việc thêm vào DB
    for (it in this) {
        newListForecastWithChangedId.add(it.copy(dateTimeInt = this.indexOf(it) + 1))
    }

    return newListForecastWithChangedId.map {
        ForecastOneDay(
            it.dateTimeInt,
            1,
            it.mainInfo.temp,
            it.mainInfo.feelsLike,
            it.mainInfo.defaultPressure,
            it.mainInfo.humidity,
            it.weatherDesc[0].mainWeatherType,
            it.weatherDesc[0].description,
            it.weatherDesc[0].icon,
            it.clouds.percent,
            it.wind.speed,
            it.wind.degree,
            it.wind.gust,
            it.visibility,
            it.precipitationProbability,
            checkForNullRain(it.rain),
            it.dateTimeText
        )
    }
}

private fun checkForNullRain(rain: Rain?): Double {
    return (rain?.last3hVolume) ?: 0.0
}

fun OpenWeatherCurrentWeatherResponse.asDatabaseModel(): CurrentWeather {
    return this.let {
        CurrentWeather(
            1,
            it.weather[0].mainWeatherType,
            it.weather[0].description,
            it.weather[0].icon,
            it.mainInfo.temp,
            it.mainInfo.feelsLike,
            it.mainInfo.defaultPressure,
            it.mainInfo.humidity,
            it.visibility,
            it.wind.speed,
            it.wind.degree,
            it.wind.gust,
            it.clouds.percent,
            it.dateTimeInt,
            it.timezone,
            it.systemInfo.country,
            it.cityName
        )
    }
}