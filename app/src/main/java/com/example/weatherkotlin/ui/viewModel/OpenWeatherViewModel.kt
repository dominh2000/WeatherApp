package com.example.weatherkotlin.ui.viewModel

import androidx.lifecycle.*
import com.example.weatherkotlin.data.domainModel.OneDayForecast
import com.example.weatherkotlin.data.domainModel.OpenWeatherCurrentWeather
import com.example.weatherkotlin.data.domainModel.OpenWeatherForecastFiveDays
import com.example.weatherkotlin.data.repository.OpenWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OpenWeatherApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class OpenWeatherViewModel @Inject constructor(
    val openWeatherRepository: OpenWeatherRepository
) : ViewModel() {

    private val _status = MutableLiveData<OpenWeatherApiStatus>()
    private val _weatherItem = MutableLiveData<OneDayForecast>()

    val status: LiveData<OpenWeatherApiStatus> = _status
    val forecast5Days: LiveData<OpenWeatherForecastFiveDays> =
        openWeatherRepository.fiveDaysForecast.asLiveData()
    val currentWeather: LiveData<OpenWeatherCurrentWeather> =
        openWeatherRepository.currentWeather.asLiveData()
    val weatherItem: LiveData<OneDayForecast> = _weatherItem

    fun getOpenWeatherInfo() {
        launchDataLoad {
            openWeatherRepository.refreshOpenWeather()
        }
    }

    fun onWeatherItemClicked(item: OneDayForecast) {
        _weatherItem.value = item
    }

    fun getOpenWeatherInfoByCoord(latt: Double, long: Double) {
        launchDataLoad {
            openWeatherRepository.getOpenWeatherByCoord(latt, long)
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

    /**
     * General dataload function to abstract the common operations when loading data from network
     */
    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            _status.value = OpenWeatherApiStatus.LOADING
            try {
                block()
                _status.value = OpenWeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = OpenWeatherApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }
}