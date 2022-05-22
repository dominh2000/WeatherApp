package com.example.weatherkotlin.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.database.ApplicationRoomDatabase.Companion.getDatabase
import com.example.weatherkotlin.domain.LocationInfo
import com.example.weatherkotlin.domain.WeatherOneDay
import com.example.weatherkotlin.repository.WeatherRepository
import kotlinx.coroutines.launch

enum class WeatherApiStatus { LOADING, ERROR, DONE }

class WeatherViewModel(app: BaseApplication) : ViewModel() {

    private val weatherRepository = WeatherRepository(app.databaseApplication)

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
                    WeatherApiStatus.DONE // Phải đặt ở ngay sau khi refresh xong nếu không sẽ không bao giờ chạy được đến sau collect
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

class WeatherViewModelFactory(val app: BaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}