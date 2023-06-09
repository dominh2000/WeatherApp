package com.example.weatherkotlin.ui.viewModel

import androidx.lifecycle.*
import com.example.weatherkotlin.data.domainModel.LocationInfo
import com.example.weatherkotlin.data.domainModel.WeatherOneDay
import com.example.weatherkotlin.data.repository.MetaWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class WeatherApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: MetaWeatherRepository
) : ViewModel() {

    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _oneWeather = MutableLiveData<WeatherOneDay>()

    val status: LiveData<WeatherApiStatus> = _status
    val allWeather: LiveData<LocationInfo> = weatherRepository.weatherByLocation.asLiveData()
    val oneWeather: LiveData<WeatherOneDay> = _oneWeather
    val todayWeather: LiveData<WeatherOneDay> = weatherRepository.weatherToday.asLiveData()

    init {
        getAllWeatherInfo()
    }

    private fun getAllWeatherInfo() {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                weatherRepository.refreshWeather()
                _status.value =
                    WeatherApiStatus.DONE
            } catch (error: Exception) {
                _status.value = WeatherApiStatus.ERROR
                error.printStackTrace()
            }
        }
    }

    fun onWeatherItemClicked(weatherOneDay: WeatherOneDay) {
        _oneWeather.value = weatherOneDay
    }
}