package com.example.weatherappsample1yt.data.model.format

import android.content.Context
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import kotlin.math.roundToInt

//hapus area dari default value dan null
data class CurrentWeatherData(
    val icon: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double?,
    val longitude: Double?,
    val temperature: TemperatureModel,
    val maxTemperature: TemperatureModel?,
    val minTemperature: TemperatureModel?,
    val weatherStatus: String? = null,
    val weatherDescription: String? = null,
    val windSpeed: Double?,
    val humidity: Int? = null,
    val precipitation: Double? = null,
)

data class TemperatureModel(val valueTemperature: Double) {
    fun formatTemperature(
        unitTemp: TemperatureUnitOptions,
    ): String {
        val formattedValue = when (unitTemp) {
            TemperatureUnitOptions.Celsius -> valueTemperature.roundToInt()
                .let { "%d°C".format(it) }

            TemperatureUnitOptions.Fahrenheit -> valueTemperature.let { ((it * 9 / 5) + 32).roundToInt() }
                .let { "%d°F".format(it) }

        }
        return formattedValue
    }
    companion object {
        fun formatTemperatureRange(
            context: Context,
            unitTemp: TemperatureUnitOptions = TemperatureUnitOptions.Celsius,
            minTemp: TemperatureModel?,
            maxTemp: TemperatureModel?
        ): String {
            val formattedMinTemp = minTemp?.formatTemperature(unitTemp)
            val formattedMaxTemp = maxTemp?.formatTemperature(unitTemp)
            return context.getString(R.string.temp_weather_day_item, formattedMinTemp, formattedMaxTemp)
        }
    }
}