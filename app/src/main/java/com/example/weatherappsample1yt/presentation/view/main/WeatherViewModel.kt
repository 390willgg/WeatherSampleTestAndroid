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
import com.example.weatherappsample1yt.domain.useCase.weather.WeatherUseCase
import com.example.weatherappsample1yt.presentation.AppState
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @AssistedInject constructor(
    @Assisted private val weatherUseCaseFlow: Flow<@JvmSuppressWildcards WeatherUseCase>,
) : ViewModel() {
    @Inject
    lateinit var appState: StateFlow<AppState>

    private val _currentWeather = MutableLiveData<CurrentWeatherData?>()
    val currentWeather: LiveData<CurrentWeatherData?> = _currentWeather

    private val _forecastWeather = MutableLiveData<ForecastWeatherData?>()
    val forecastWeather: LiveData<ForecastWeatherData?> = _forecastWeather

    private val _temperatureUnit = MutableLiveData<TemperatureUnitOptions?>()
    val temperatureUnit: LiveData<TemperatureUnitOptions?> = _temperatureUnit

    private lateinit var weatherUseCase: WeatherUseCase

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    init {
        viewModelScope.launch {
            try {
                launch {
                    weatherUseCaseFlow.collect {
                        weatherUseCase = it
                        fetchWeatherData()
                        appState.asLiveData().observeForever { appState ->
                            _temperatureUnit.value = appState.temperatureUnitOptions
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error observing preferences", e)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            weatherUseCase: Flow<@JvmSuppressWildcards WeatherUseCase>
        ): WeatherViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory, weatherUseCase: Flow<@JvmSuppressWildcards WeatherUseCase>,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(weatherUseCase) as T
            }
        }
    }

    private fun fetchWeatherData() {
        viewModelScope.launch {
            try {
                val currentWeatherDeferred =
                    async { weatherUseCase.getCurrentWeather(lat, lon, "metric") }
                val forecastWeatherDeferred =
                    async { weatherUseCase.getForecastWeather(lat, lon, "metric") }

                _currentWeather.value = currentWeatherDeferred.await()
                _forecastWeather.value = forecastWeatherDeferred.await()
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
        fetchWeatherData()
    }

    fun getForecastWeather() {
        fetchWeatherData()
    }
}