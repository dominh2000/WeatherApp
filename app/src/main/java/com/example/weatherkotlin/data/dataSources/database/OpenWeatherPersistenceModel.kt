package com.example.weatherkotlin.data.dataSources.database

import androidx.room.*
import com.example.weatherkotlin.data.domainModel.*
import com.example.weatherkotlin.util.convertFromDateTimeIntToPattern2
import com.example.weatherkotlin.util.convertFromDateTimeIntToPattern3

@Entity(tableName = "city_for_forecast_5_days")
data class CityForForecastFiveDays(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "city_name") val cityName: String
)

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey @ColumnInfo(name = "city_id") val cityId: Int,
    @ColumnInfo(name = "weather_type") val mainWeatherType: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "feels_like") val feelsLike: Double,
    @ColumnInfo(name = "default_pressure") val defaultPressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "wind_degree") val windDegree: Int,
    @ColumnInfo(name = "wind_gust") val windGust: Double,
    @ColumnInfo(name = "cloud_percent") val cloudAmountPercent: Int,
    @ColumnInfo(name = "date_time_int") val dateTimeInt: Int,
    @ColumnInfo(name = "time_zone", defaultValue = "0") val timeZone: Int,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "city_name") val cityName: String
)

@Entity(tableName = "forecast_1_day")
data class ForecastOneDay(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "fk_5_days") val fk5Days: Int,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "feels_like") val feelsLike: Double,
    @ColumnInfo(name = "default_pressure") val defaultPressure: Int,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "weather_type") val mainWeatherType: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "cloud_percent") val cloudAmountPercent: Int,
    @ColumnInfo(name = "wind_speed") val windSpeed: Double,
    @ColumnInfo(name = "wind_degree") val windDegree: Int,
    @ColumnInfo(name = "wind_gust") val windGust: Double,
    @ColumnInfo(name = "visibility") val visibility: Int,
    @ColumnInfo(name = "precipitation") val precipitation: Double,
    @ColumnInfo(name = "rain_volume_3h") val last3hRainVolume: Double,
    @ColumnInfo(name = "date_time_text") val dateTimeText: String
)

data class FullForecastFiveDays(
    @Embedded val city: CityForForecastFiveDays,
    @Relation(
        parentColumn = "id",
        entityColumn = "fk_5_days"
    )
    val forecast5Days: List<ForecastOneDay>
)

// Extension functions
fun FullForecastFiveDays.asDomainModel(): OpenWeatherForecastFiveDays{
    return this.let {
        OpenWeatherForecastFiveDays(
            it.city.id,
            it.forecast5Days.asDomainModel(),
            it.city.cityName
        )
    }
}

fun CurrentWeather.asDomainModel(): OpenWeatherCurrentWeather{
    return this.let {
        OpenWeatherCurrentWeather(
            it.cityId,
            WeatherState(
                it.mainWeatherType,
                it.description,
                it.icon
            ),
            ForecastInfo(
                it.temp,
                it.feelsLike,
                it.defaultPressure,
                it.humidity
            ),
            it.visibility,
            WindInfo(
                it.windSpeed,
                it.windDegree,
                it.windGust
            ),
            it.cloudAmountPercent,
            it.dateTimeInt.convertFromDateTimeIntToPattern2(it.timeZone),
            it.dateTimeInt.convertFromDateTimeIntToPattern3(it.timeZone),
            it.country,
            it.cityName
        )
    }
}

fun List<ForecastOneDay>.asDomainModel(): List<OneDayForecast>{
    return this.map {
        OneDayForecast(
            it.id,
            ForecastInfo(
                it.temp,
                it.feelsLike,
                it.defaultPressure,
                it.humidity
            ),
            WeatherState(
                it.mainWeatherType,
                it.description,
                it.icon
            ),
            it.cloudAmountPercent,
            WindInfo(
                it.windSpeed,
                it.windDegree,
                it.windGust
            ),
            it.visibility,
            it.precipitation,
            it.last3hRainVolume,
            it.dateTimeText
        )
    }
}
