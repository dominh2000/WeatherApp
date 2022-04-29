package com.example.weatherkotlin.util

import com.example.weatherkotlin.network.TotalWeather
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private const val DATE_FORMAT_PATTERN = "E, d MMM"
private const val DOUBLE_NUMBER_FORM = "%.2f"
private val weatherStates = mapOf(
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

fun String.convertDate(): String {
    val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
    val tokenizer = StringTokenizer(this, "-")
    val listParam = mutableListOf<Int>()
    while (tokenizer.hasMoreTokens()) {
        listParam.add(tokenizer.nextToken().toInt())
    }
    val calendar = GregorianCalendar(
        listParam.elementAt(0),
        listParam.elementAt(1) - 1,
        listParam.elementAt(2)
    )
    return formatter.format(calendar.time)
}

fun showTemperature(minTemp: Double, maxTemp: Double): String {
    return minTemp.roundToInt().toString().plus("°C / ").plus(maxTemp.roundToInt().toString())
        .plus("°C")
}

fun Double.convertToDoubleForm(): Double {
    return this.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
}

fun Double.convertToString(): String {
    return DOUBLE_NUMBER_FORM.format(Locale.US, this)
}

fun String.convertWeatherStateIntoVie(): String {
    return weatherStates.get(this)!!
}