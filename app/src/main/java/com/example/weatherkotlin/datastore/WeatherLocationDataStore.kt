package com.example.weatherkotlin.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherkotlin.const.WEATHER_LOCATION_PREFERENCES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.weatherDataStore: DataStore<Preferences> by preferencesDataStore(
    name = WEATHER_LOCATION_PREFERENCES
)

class WeatherLocationDataStore(ctx: Context) {
    private val LOCATION_LAT = doublePreferencesKey("weather_location_lat")
    private val LOCATION_LON = doublePreferencesKey("weather_location_lon")

    suspend fun saveWeatherLocationToPreferencesStore(
        lat: Double,
        lon: Double,
        ctx: Context
    ) {
        ctx.weatherDataStore.edit {
            it[LOCATION_LAT] = lat
            it[LOCATION_LON] = lon
        }
    }

    val locationFlow: Flow<List<Double>> = ctx.weatherDataStore.data
        .catch {
            it.printStackTrace()
            emit(emptyPreferences())
        }
        .map {
            if (it[LOCATION_LAT] == null && it[LOCATION_LON] == null) {
                listOf(0.0, 0.0)
            } else {
                listOf(it[LOCATION_LAT]!!, it[LOCATION_LON]!!)
            }
        }
}