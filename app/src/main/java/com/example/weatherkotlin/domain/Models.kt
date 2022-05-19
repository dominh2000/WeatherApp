package com.example.weatherkotlin.domain

import com.example.weatherkotlin.database.DatabaseTask

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

data class Task(
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: Int,
    val deadlineDate: String,
    val deadlineHour: String,
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
            it.completed
        )
    }
}