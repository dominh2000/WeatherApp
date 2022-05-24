package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.*
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.domain.OneDayForecast
import com.example.weatherkotlin.domain.OpenWeatherCurrentWeather
import com.example.weatherkotlin.domain.OpenWeatherForecastFiveDays
import com.example.weatherkotlin.repository.OpenWeatherRepository
import kotlinx.coroutines.launch

enum class OpenWeatherApiStatus { LOADING, ERROR, DONE }

class OpenWeatherViewModel(app: BaseApplication) : ViewModel() {

    private val openWeatherRepository = OpenWeatherRepository(app.databaseApplication)

    private val _status = MutableLiveData<OpenWeatherApiStatus>()
    private val _weatherItem = MutableLiveData<OneDayForecast>()

    val status: LiveData<OpenWeatherApiStatus> = _status
    val forecast5Days: LiveData<OpenWeatherForecastFiveDays> =
        openWeatherRepository.fiveDaysForecast.asLiveData()
    val currentWeather: LiveData<OpenWeatherCurrentWeather> =
        openWeatherRepository.currentWeather.asLiveData()
    val weatherItem: LiveData<OneDayForecast> = _weatherItem

    init {
        getOpenWeatherInfo()
    }

    fun getOpenWeatherInfo() {
        viewModelScope.launch {
            _status.value = OpenWeatherApiStatus.LOADING
            try {
                openWeatherRepository.refreshOpenWeather()
                _status.value = OpenWeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = OpenWeatherApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun onWeatherItemClicked(item: OneDayForecast) {
        _weatherItem.value = item
    }

    fun getOpenWeatherInfoByCoord(latt: Double, long: Double) {
        viewModelScope.launch {
            _status.value = OpenWeatherApiStatus.LOADING
            try {
                openWeatherRepository.getOpenWeatherByCoord(latt, long)
                _status.value = OpenWeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = OpenWeatherApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun getFormattedPrecipitation(unformattedPrecipitation: String): String {
        if (unformattedPrecipitation.length == 3) {
            return if (unformattedPrecipitation == "1.0") {
                "100%"
            } else if (unformattedPrecipitation[2] != '0') {
                unformattedPrecipitation[2].toString().plus("0%")
            } else {
                unformattedPrecipitation[2].toString().plus("%")
            }
        } else {
            val precipitation =
                weatherItem.value!!.precipitation.toString().substring(2, 4)
            return if (precipitation[0] == '0') {
                precipitation[1].toString().plus("%")
            } else {
                precipitation.plus("%")
            }
        }
    }
}

class OpenWeatherViewModelFactory(val app: BaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OpenWeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OpenWeatherViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}