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
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

private const val DATE_FORMAT_PATTERN = "E, d MMM"
private const val DATE_FORMAT_PATTERN_1 = "E, d MMM, y"
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

fun configDateButton(ctx: Context, textView: TextView) {
    val datePickerDialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        DatePickerDialog(ctx)
    } else {
        TODO("VERSION.SDK_INT < N")
    }
    datePickerDialog.setOnDateSetListener { _, i, i2, i3 ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, i)
        calendar.set(Calendar.MONTH, i2)
        calendar.set(Calendar.DAY_OF_MONTH, i3)
        val selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        textView.text = selectedDate
    }
    datePickerDialog.show()
}

fun configTimeButton(ctx: Context, textView: TextView) {
    val timePickerDialog = TimePickerDialog(
        ctx,
        { _, hourOfDay, minute ->
            val formattedTime = when {
                (hourOfDay < 10) -> {
                    if (minute > 10) {
                        "0$hourOfDay:$minute"
                    } else {
                        "0$hourOfDay:0$minute"
                    }
                }
                else -> {
                    if (minute > 10) {
                        "$hourOfDay:$minute"
                    } else {
                        "$hourOfDay:0$minute"
                    }
                }
            }
            textView.text = formattedTime
        },
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        Calendar.getInstance().get(Calendar.MINUTE),
        true
    )
    timePickerDialog.show()
}