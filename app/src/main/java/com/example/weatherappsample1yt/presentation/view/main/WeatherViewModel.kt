package com.example.weatherappsample1yt.presentation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    
    init {
        viewModelScope.launch {
            try {
                launch {
                    preferencesUseCase.observeApiPreferences().collect {
                        when (it) {
                            ApiProviderOptions.OPEN_WEATHER -> owWeatherUseCase
                            ApiProviderOptions.AI_METEOSOURCE -> amsWeatherUseCase
                            ApiProviderOptions.WEATHER_API -> waWeatherUseCase
                            else -> owWeatherUseCase
                        }
                    }
                }
                
                launch {
                    preferencesUseCase.observeTemperaturePreferences().collect {
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
    
    private suspend fun getWeatherUseCase() : WeatherUseCase {
        val apiProvider = preferencesUseCase.getApiPreferences()
        return when (apiProvider) {
            ApiProviderOptions.OPEN_WEATHER -> owWeatherUseCase
            ApiProviderOptions.AI_METEOSOURCE -> amsWeatherUseCase
            ApiProviderOptions.WEATHER_API -> waWeatherUseCase
            else -> owWeatherUseCase
        }
    }
    
    fun updateApiProvider(apiProvider : ApiProviderOptions) {
        viewModelScope.launch {
            try {
                preferencesUseCase.saveApiPreferences(apiProvider)
            }
            catch (e : Exception) {
                Log.e("WeatherViewModel", "Error updating API provider", e)
            }
        }
    }
    
    fun updateTemperatureUnit(unit : TemperatureUnitOptions) {
        viewModelScope.launch {
            try {
                preferencesUseCase.saveTemperaturePreferences(unit)
            }
            catch (e : Exception) {
                // Handle the exception
                Log.e("WeatherViewModel", "Error updating temperature unit", e)
            }
        }
    }
    
    fun loadTemperatureUnit() {
        viewModelScope.launch {
            try {
                val temperatureUnit = preferencesUseCase.getTemperaturePreferences()
                _temperatureUnit.postValue(temperatureUnit)
            }
            catch (e : Exception) {
                Log.e("WeatherViewModel", "Error loading temperature unit", e)
            }
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val units = when (_temperatureUnit.value) {
                    TemperatureUnitOptions.Fahrenheit -> "imperial"
                    else -> "metric"
                }
                val currentWeather = getWeatherUseCase().getCurrentWeather(lat, lon, units)
                val updateWeather = currentWeather?.convertTemperature(_temperatureUnit.value ?: TemperatureUnitOptions.Celsius)
                _currentWeather.postValue(currentWeather)
            } catch (e: Exception) {
                // Handle the exception
                Log.e("WeatherViewModel", "Error getting current weather", e)
            }
        }
    }

    fun getForecastWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val units = when (_temperatureUnit.value) {
                    TemperatureUnitOptions.Fahrenheit -> "imperial"
                    else -> "metric"
                }
                val forecastWeather = getWeatherUseCase().getForecastWeather(lat, lon, units)
                val updateWeather = forecastWeather?.convertTemperature(_temperatureUnit.value ?: TemperatureUnitOptions.Celsius)
                _forecastWeather.postValue(forecastWeather)
            } catch (e: Exception) {
                // Handle the exception
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
    OPEN_WEATHER, AI_METEOSOURCE, WEATHER_API
}

enum class TemperatureUnitOptions {
    Celsius, Fahrenheit
}