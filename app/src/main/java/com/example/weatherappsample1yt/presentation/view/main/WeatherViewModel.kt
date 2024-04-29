package com.example.weatherappsample1yt.presentation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Named

class WeatherViewModel @AssistedInject constructor(
    @Named("OWWeather") private val owWeatherUseCase: WeatherUseCase,
    @Named("WAWeather") private val waWeatherUseCase: WeatherUseCase,
    @Named("AMSWeather") private val amsWeatherUseCase: WeatherUseCase,
    @Assisted private val apiOptions: ApiProviderOptions
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(apiOptions: ApiProviderOptions): WeatherViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory, apiOptions: ApiProviderOptions
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(apiOptions) as T
            }
        }
    }

    private fun getWeatherUseCase(): WeatherUseCase {
        return when (apiOptions) {
            ApiProviderOptions.OPEN_WEATHER -> owWeatherUseCase
            ApiProviderOptions.AI_METEOSOURCE -> amsWeatherUseCase
            ApiProviderOptions.WEATHER_API -> waWeatherUseCase
        }
    }

    private val _currentWeather = MutableLiveData<CurrentWeatherData?>()
    val currentWeather: LiveData<CurrentWeatherData?> = _currentWeather

    private val _forecastWeather = MutableLiveData<ForecastWeatherData?>()
    val forecastWeather: LiveData<ForecastWeatherData?> = _forecastWeather

    fun getCurrentWeather(lat: Double, lon: Double, units: String) {
        viewModelScope.launch {
            try {
                val currentWeather = getWeatherUseCase().getCurrentWeather(lat, lon, units)
            _currentWeather.postValue(currentWeather)
            } catch (e: Exception) {
                // Handle the exception
                Log.e("WeatherViewModel", "Error getting current weather", e)
            }
        }
    }

    fun getForecastWeather(lat: Double, lon: Double, units: String) {
        viewModelScope.launch {
            try {
                val forecastWeather = getWeatherUseCase().getForecastWeather(lat, lon, units)
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