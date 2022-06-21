package com.example.weatherkotlin.const

const val DATE_FORMAT_PATTERN = "E, d MMM"
const val DATE_FORMAT_PATTERN_1 = "yyyy-MM-dd"
const val DATE_FORMAT_PATTERN_2 = "E, d MMM, yyyy"
const val DATE_FORMAT_PATTERN_3 = "HH:mm, d MMM, yyyy"
const val DATE_FORMAT_PATTERN_4 = "HH:mm, E, d MMM, yyyy"
const val DATE_FORMAT_PATTERN_5 = "yyyy-MM-dd HH:mm"
const val DOUBLE_NUMBER_FORM = "%.2f"
const val WEATHER_LOCATION_PREFERENCES = "weather_location_preferences"

val weatherStates = mapOf(
    "sn" to "Tuyết rơi",
    "sl" to "Mưa tuyết",
    "h" to "Mưa đá",
    "t" to "Bão có sấm sét và mưa lớn",
    "hr" to "Mưa lớn",
    "lr" to "Mưa nhỏ",
    "s" to "Mưa rào",
    "hc" to "Nhiều mây",
    "lc" to "Có mây",
    "c" to "Trời quang"
)
val weatherIconMap = mapOf(
    "01d" to "clearsky_day",
    "01n" to "clearsky_night",
    "02d" to "fair_day",
    "02n" to "fair_night",
    "03d" to "cloudy",
    "03n" to "cloudy",
    "04d" to "cloudy",
    "04n" to "cloudy",
    "09d" to "rainshowers_day",
    "09n" to "rainshowers_night",
    "10d" to "rain",
    "10n" to "rain",
    "11d" to "heavyrainandthunder",
    "11n" to "heavyrainandthunder",
    "13d" to "snow",
    "13n" to "snow",
    "50d" to "fog",
    "50n" to "fog",
)