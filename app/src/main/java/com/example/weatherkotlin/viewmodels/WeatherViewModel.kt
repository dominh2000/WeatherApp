package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.*
import com.example.weatherkotlin.network.ResponseData
import com.example.weatherkotlin.network.TotalWeather
import com.example.weatherkotlin.network.WeatherApi
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

enum class WeatherApiStatus { LOADING, ERROR, DONE }
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

class WeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _response = MutableLiveData<ResponseData?>()
    private val _allWeather = MutableLiveData<List<TotalWeather>>()
    private val _oneWeather = MutableLiveData<TotalWeather>()
    private val _todayWeather = MutableLiveData<TotalWeather?>()

    val status: LiveData<WeatherApiStatus> = _status
    val allWeather: LiveData<List<TotalWeather>> = Transformations.map(_allWeather) {
        convertAllWeather(it)
    }
    val oneWeather: LiveData<TotalWeather> = _oneWeather
    val todayWeather: LiveData<TotalWeather?> = _todayWeather

    init {
        getAllWeatherInfo()
    }

    private fun getAllWeatherInfo() {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                _response.value = WeatherApi.retrofitServices.getWeatherByLocation()
                _allWeather.value = _response.value!!.totalWeather
                _todayWeather.value = allWeather.value!!.get(0)
                _status.value = WeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
                _response.value = null
                _todayWeather.value = null
                _allWeather.value = listOf()
            }
        }
    }

    fun onWeatherItemClicked(weather: TotalWeather) {
        _oneWeather.value = weather
    }

    private fun convertAllWeather(allWeather: List<TotalWeather>): List<TotalWeather> {
        val newAllWeather = mutableListOf<TotalWeather>()
        for (weather in allWeather) {
            newAllWeather.add(convertWeather(weather))
        }
        return newAllWeather
    }

    private fun convertWeather(weather: TotalWeather): TotalWeather {
        return TotalWeather(
            weather.id,
            convertWeatherStateIntoVie(weather.stateAbbr),
            weather.stateAbbr,
            weather.windDirectionCompass,
            weather.timeStampCreated,
            convertDate(weather.date),
            convertDouble(weather.minTemp),
            convertDouble(weather.maxTemp),
            convertDouble(weather.theTemp),
            convertDouble(weather.windSpeed),
            convertDouble(weather.windDirection),
            convertDouble(weather.airPressure),
            weather.humidity,
            convertDouble(weather.visibility),
            weather.predictability
        )
    }

    private fun convertDate(date: String): String {
        val formatter = SimpleDateFormat("E, d MMM", Locale.getDefault())
        val tokenizer = StringTokenizer(date, "-")
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

    private fun convertDouble(number: Double): Double {
        return number.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    private fun convertWeatherStateIntoVie(stateAbbr: String): String {
        return weatherStates.get(stateAbbr)!!
    }
}