package com.example.weatherappsample1yt.presentation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Named

class WeatherViewModel @AssistedInject constructor(
    @Named("OWWeather") private val owWeatherUseCase : WeatherUseCase,
    @Named("WAWeather") private val waWeatherUseCase : WeatherUseCase,
    @Named("AMSWeather") private val amsWeatherUseCase : WeatherUseCase,
    @Assisted private val preferencesUseCase : PreferencesUseCase,
) : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeatherData?>()
    val currentWeather : LiveData<CurrentWeatherData?> = _currentWeather
    
    private val _forecastWeather = MutableLiveData<ForecastWeatherData?>()
    val forecastWeather : LiveData<ForecastWeatherData?> = _forecastWeather

    private val _temperatureUnit = MutableLiveData<TemperatureUnitOptions?>()
    val temperatureUnit : LiveData<TemperatureUnitOptions?> = _temperatureUnit

    private var lat: Double = 0.0
    private var lon: Double = 0.0
    
    init {
        viewModelScope.launch {
            try {
                launch {
                    preferencesUseCase.observeApiPreferences().asLiveData()
                        .observeForever { apiProvider ->
                            if (apiProvider != null) {
                                fetchWeatherData(apiProvider)
                        }
                    }
                }
                
                launch {
                    preferencesUseCase.observeTemperaturePreferences().asLiveData().observeForever {
                        _temperatureUnit.value = it
                    }
                }
            }
            catch (e : Exception) {
                Log.e("WeatherViewModel", "Error observing preferences", e)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(preferencesUseCase : PreferencesUseCase) : WeatherViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory : Factory, preferencesUseCase : PreferencesUseCase,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(preferencesUseCase) as T
            }
        }
    }

    private fun getWeatherUseCase(apiProvider: ApiProviderOptions): WeatherUseCase {
        return when (apiProvider) {
            ApiProviderOptions.OPEN_WEATHER -> owWeatherUseCase
            ApiProviderOptions.AI_METEOSOURCE -> amsWeatherUseCase
            ApiProviderOptions.WEATHER_API -> waWeatherUseCase
        }
    }

    private fun fetchWeatherData(apiProvider: ApiProviderOptions) {
        viewModelScope.launch {
            try {
                val weatherUseCase = getWeatherUseCase(apiProvider)
                val currentWeather = weatherUseCase.getCurrentWeather(lat, lon, "metric")
                _currentWeather.postValue(currentWeather)

                val forecastWeather = weatherUseCase.getForecastWeather(lat, lon, "metric")
                _forecastWeather.postValue(forecastWeather)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
        viewModelScope.launch {
            try {
                val apiProvider = preferencesUseCase.getApiPreferences()
                apiProvider?.let { fetchWeatherData(it) }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error getting current weather", e)
            }
        }
    }

    fun getForecastWeather() {
        viewModelScope.launch {
            try {
                val apiProvider = preferencesUseCase.getApiPreferences()
                apiProvider?.let { fetchWeatherData(it) }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error getting forecast weather", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}

enum class ApiProviderOptions {
    OPEN_WEATHER, AI_METEOSOURCE, WEATHER_API;

    override fun toString(): String {
        return when (this) {
            OPEN_WEATHER -> "Open Weather"
            AI_METEOSOURCE -> "Ai Meteosource"
            WEATHER_API -> "Weather Api"
        }
    }
}

enum class TemperatureUnitOptions {
    Celsius, Fahrenheit;

    override fun toString(): String {
        return when (this) {
            Celsius -> "Celsius"
            Fahrenheit -> "Fahrenheit"
        }
    }
}