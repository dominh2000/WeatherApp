package com.example.weatherkotlin.data.domainModel

/*
    For OpenWeather API
 */
data class OpenWeatherForecastFiveDays(
    val id: Int,
    val fiveDaysForecast: List<OneDayForecast>,
    val cityName: String,
)

data class OpenWeatherCurrentWeather(
    val cityId: Int,
    val currentWeatherDescription: WeatherState,
    val forecastInfo: ForecastInfo,
    val visibility: Int,
    val wind: WindInfo,
    val cloudAmountPercent: Int,
    val dateTime: String,
    val updatedDateTime: String,
    val country: String,
    val cityName: String
)

data class OneDayForecast(
    val id: Int,
    val mainForecastInfo: ForecastInfo,
    val weatherDescription: WeatherState,
    val cloudAmountPercent: Int,
    val wind: WindInfo,
    val visibility: Int,
    val precipitation: Double,
    val last3hRainVolume: Double,
    val dateTimeText: String,
)

data class ForecastInfo(
    val temp: Double,
    val feelsLike: Double,
    val defaultPressure: Int,
    val humidity: Int
)

data class WeatherState(
    val mainWeatherType: String,
    val description: String,
    val icon: String
)

data class WindInfo(
    val speed: Double,
    val degree: Int,
    val gust: Double
)