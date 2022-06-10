package com.example.weatherkotlin.util

import com.example.weatherkotlin.const.*
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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

fun calculateMillisecondsFromDate(date: String, time: String): Long {
    val tokenizerDate = StringTokenizer(date, "-")
    val listParamDate = mutableListOf<Int>()
    while (tokenizerDate.hasMoreTokens()) {
        listParamDate.add(tokenizerDate.nextToken().toInt())
    }
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, listParamDate[0])
    calendar.set(Calendar.MONTH, listParamDate[1] - 1)
    calendar.set(Calendar.DAY_OF_MONTH, listParamDate[2])

    val tokenizerTime = StringTokenizer(time, ":")
    val listParamTime = mutableListOf<Int>()
    while (tokenizerTime.hasMoreTokens()) {
        listParamTime.add(tokenizerTime.nextToken().toInt())
    }
    calendar.set(Calendar.HOUR_OF_DAY, listParamTime[0])
    calendar.set(Calendar.MINUTE, listParamTime[1])
    calendar.set(Calendar.SECOND, 0)

    /* TODO: For testing
    val calendar1 = Calendar.getInstance()
    calendar1.timeInMillis = calendar.timeInMillis
    val formatter = SimpleDateFormat("dd:MM:yy:HH:mm:ss", Locale.getDefault())
    println(formatter.format(calendar1.time))
     */
    return calendar.timeInMillis
}

fun calculateMillisecondsForAdvancedSearch(date: String): Long {
    val tokenizerDate = StringTokenizer(date, "-")
    val listParamDate = mutableListOf<Int>()
    while (tokenizerDate.hasMoreTokens()) {
        listParamDate.add(tokenizerDate.nextToken().toInt())
    }

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, listParamDate[0])
    calendar.set(Calendar.MONTH, listParamDate[1] - 1)
    calendar.set(Calendar.DAY_OF_MONTH, listParamDate[2])

    return calendar.timeInMillis
}

fun calculateCurrentTimeMilliseconds(): Long {
    val calendar = Calendar.getInstance()
    return calendar.timeInMillis
}

fun String.convertFromPattern1ToFullDate(): String {
    val date = SimpleDateFormat(DATE_FORMAT_PATTERN_1, Locale.getDefault()).parse(this)
    return DateFormat.getDateInstance(DateFormat.FULL).format(date?.time)
}

fun Int.convertFromDateTimeIntToPattern2(shiftInSeconds: Int): String {
    val time = this.toLong() * 1000 + shiftInSeconds.toLong() * 1000
    val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN_2, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(time))
}

fun Int.convertFromDateTimeIntToPattern3(shiftInSeconds: Int): String {
    val time = this.toLong() * 1000 + shiftInSeconds.toLong() * 1000
    val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN_3, Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(time))
}

fun String.convertDateFromPattern5ToPattern4(): String {
    val date = SimpleDateFormat(DATE_FORMAT_PATTERN_5, Locale.getDefault()).parse(this)
    val formatter = SimpleDateFormat(DATE_FORMAT_PATTERN_4, Locale.getDefault())
    return formatter.format(date!!)
}