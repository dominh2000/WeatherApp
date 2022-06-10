package com.example.weatherkotlin.database

import androidx.room.*
import com.example.weatherkotlin.domain.LocationInfo
import com.example.weatherkotlin.domain.WeatherOneDay

@Entity(tableName = "weather_response")
data class DatabaseLocationInfo(
    @PrimaryKey @ColumnInfo(name = "woe_id") val woeId: Int,
    @ColumnInfo(name = "time") val currentTime: String,
    @ColumnInfo(name = "sun_rise") val sunRise: String,
    @ColumnInfo(name = "sun_set") val sunSet: String,
    @ColumnInfo(name = "timezone_name") val timeZoneName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "locationType") val locationType: String,
    @ColumnInfo(name = "latt_long") val lattLong: String,
    @ColumnInfo(name = "timezone") val timeZone: String
)

@Entity(tableName = "weather_one_day")
data class DatabaseWeatherOneDay(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "location_id") val locationId: Int,
    @ColumnInfo(name = "state_name") val stateName: String,
    @ColumnInfo(name = "state_abbr") val stateAbbr: String,
    @ColumnInfo(name = "wind_direction_compass") val windDirectionCompass: String,
    @ColumnInfo(name = "created_on") val timeStampCreated: String,
    @ColumnInfo(name = "applicable_date") val date: String,
    @ColumnInfo(name = "min_temp") val minTemp: Double,
    @ColumnInfo(name = "max_temp") val maxTemp: Double,
    @ColumnInfo(name = "the_temp") val theTemp: Double,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "wind_direction") val windDirection: Double,
    @ColumnInfo(name = "air_pressure") val airPressure: Double,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "visibility") val visibility: Double,
    @ColumnInfo(name = "predictability") val predictability: Int
)

data class DatabaseFullWeather(
    @Embedded val location: DatabaseLocationInfo,
    @Relation(
        parentColumn = "woe_id",
        entityColumn = "location_id"
    )
    val weatherSixDays: List<DatabaseWeatherOneDay>
)

// Extension functions
fun DatabaseFullWeather.asDomainModel(): LocationInfo {
    return this.let {
        LocationInfo(
            it.location.woeId,
            it.weatherSixDays.asDomainModel(),
            it.location.currentTime,
            it.location.sunRise,
            it.location.sunSet,
            it.location.timeZoneName,
            it.location.title,
            it.location.locationType,
            it.location.lattLong,
            it.location.timeZone
        )
    }
}


fun List<DatabaseWeatherOneDay>.asDomainModel(): List<WeatherOneDay> {
    return this.map {
        WeatherOneDay(
            it.id,
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