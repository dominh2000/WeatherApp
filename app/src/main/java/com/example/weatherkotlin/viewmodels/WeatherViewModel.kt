package com.example.weatherkotlin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.domain.LocationInfo
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.repository.WeatherRepository
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(app: Application) : ViewModel() {

    private val weatherRepository = WeatherRepository(getDatabase(app))

    private val _status = MutableLiveData<WeatherApiStatus>()
    private val _oneWeather = MutableLiveData<WeatherOneDay>()
    private val _todayWeather = MutableLiveData<WeatherOneDay>()

    val status: LiveData<WeatherApiStatus> = _status
    val allWeather: LiveData<LocationInfo> = weatherRepository.weatherByLocation.asLiveData()
    val oneWeather: LiveData<WeatherOneDay> = _oneWeather
    val todayWeather: LiveData<WeatherOneDay> = _todayWeather

    init {
        getAllWeatherInfo()
    }

    private fun getAllWeatherInfo() {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                weatherRepository.refreshWeather()
                _status.value =
                    WeatherApiStatus.DONE // Phải đặt ở ngay sau khi refresh xong nếu không sẽ không bao giờ chạy được đến sau collect
                weatherRepository.weatherByLocation.collect {
                    _todayWeather.value = it.weatherSixDays.get(0)
                }
            } catch (error: Exception) {
                _status.value = WeatherApiStatus.ERROR
            }
        }
    }

    fun onWeatherItemClicked(weatherOneDay: WeatherOneDay) {
        _oneWeather.value = weatherOneDay
    }
}

class WeatherViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}