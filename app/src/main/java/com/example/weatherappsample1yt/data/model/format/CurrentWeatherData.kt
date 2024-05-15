package com.example.weatherappsample1yt.data.model.format

import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions

data class CurrentWeatherData(
    val icon: String?,
    val city: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?,
    val temperature: Double?,
    val maxTemperature: Double?,
    val minTemperature: Double?,
    val weatherStatus: String?,
    val weatherDescription: String?,
    val weatherIcon: String?,
    val windSpeed: Double?,
    val humidity: Int?,
    val precipitation: Double?,
) {
    fun convertTemperature(unitTemp : TemperatureUnitOptions) : CurrentWeatherData {
        val temp = if (unitTemp == TemperatureUnitOptions.Celsius) {
            temperature
        } else {
            temperature?.times(9 / 5)?.minus(32)
        }
        val maxTemp = if (unitTemp == TemperatureUnitOptions.Celsius) {
            maxTemperature
        } else {
            maxTemperature?.times(9 / 5)?.minus(32)
        }
        val minTemp = if (unitTemp == TemperatureUnitOptions.Celsius) {
            minTemperature
        } else {
            minTemperature?.times(9 / 5)?.minus(32)
        }
        
        return this.copy(
            temperature = temp, maxTemperature = maxTemp, minTemperature = minTemp
        )
    }
}