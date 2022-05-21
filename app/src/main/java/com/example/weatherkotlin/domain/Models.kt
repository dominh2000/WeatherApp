package com.example.weatherkotlin.domain

import com.example.weatherkotlin.database.DatabaseTask

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
/*
    For ToDoList
 */
data class Task(
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: Int,
    val deadlineDate: String,
    val deadlineHour: String,
    val isNotified: Boolean,
    val completed: Boolean
)

fun Task.asDatabaseModel(): DatabaseTask {
    return this.let {
        DatabaseTask(
            it.id,
            it.name,
            it.description,
            it.priority,
            it.deadlineDate,
            it.deadlineHour,
            it.isNotified,
            it.completed
        )
    }
}