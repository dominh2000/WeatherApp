package com.example.weatherkotlin.util

import com.example.weatherkotlin.network.TotalWeather

fun List<TotalWeather>.convert(): List<TotalWeather> {
    return this.map {
        TotalWeather(
            it.id,
            it.stateAbbr.convertWeatherStateIntoVie(),
            it.stateAbbr,
            it.windDirectionCompass,
            it.timeStampCreated,
            it.date.convertDate(),
            it.minTemp.convertToDoubleForm(),
            it.maxTemp.convertToDoubleForm(),
            it.theTemp.convertToDoubleForm(),
            it.windSpeed.convertToDoubleForm(),
            it.windDirection.convertToDoubleForm(),
            it.airPressure.convertToDoubleForm(),
            it.humidity,
            it.visibility.convertToDoubleForm(),
            it.predictability
        )
    }
}