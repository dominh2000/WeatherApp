package com.example.weatherkotlin.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import com.example.weatherkotlin.R
import com.example.weatherkotlin.fragments.FragmentListToDoDirections
import com.example.weatherkotlin.network.TotalWeather
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private const val DATE_FORMAT_PATTERN = "E, d MMM"
const val DATE_FORMAT_PATTERN_1 = "yyyy-MM-dd"
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

fun launchLogoutAlertDialog(ctx: Context, view: View, navController: NavController) {
    val alertDialogBuilder = MaterialAlertDialogBuilder(ctx)
        .setIcon(R.drawable.ic_warning)
        .setTitle("Thông báo")
        .setMessage("Bạn có chắc chắc muốn đăng xuất?")
        .setPositiveButton("Có") { _, _ ->
            AuthUI.getInstance().signOut(ctx)
            val action = FragmentListToDoDirections.actionFragmentListToDoToFragmentToDoStart()
            navController.navigate(action)
            Snackbar.make(ctx, view, "Đăng xuất thành công!", Snackbar.LENGTH_SHORT).show()
        }
        .setNegativeButton("Không") { _, _ -> }
        .create()
    alertDialogBuilder.show()
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

fun calculateCurrentTimeMilliseconds(): Long {
    val calendar = Calendar.getInstance()
    return calendar.timeInMillis
}

fun String.convertFromPattern1ToFullDate(): String {
    val date = SimpleDateFormat(DATE_FORMAT_PATTERN_1, Locale.getDefault()).parse(this)
    return DateFormat.getDateInstance(DateFormat.FULL).format(date?.time)
}