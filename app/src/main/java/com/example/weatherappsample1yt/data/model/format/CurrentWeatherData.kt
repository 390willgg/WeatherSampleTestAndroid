package com.example.weatherappsample1yt.data.model.format

import android.content.Context
import androidx.annotation.StringRes
import com.example.weatherappsample1yt.R
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import kotlin.math.roundToInt

data class CurrentWeatherData(
    val icon: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val temperature: TemperatureModel? = null,
    val maxTemperature: TemperatureModel? = null,
    val minTemperature: TemperatureModel? = null,
    val weatherStatus: String? = null,
    val weatherDescription: String? = null,
    val weatherIcon: String? = null,
    val windSpeed: Double? = null,
    val humidity: Int? = null,
    val precipitation: Double? = null,
)

private fun formatSingleTemperature(
    valueTemperature: Double?,
    unitTemp: TemperatureUnitOptions?
): String {
    val formattedValue = when (unitTemp) {
        TemperatureUnitOptions.Celsius -> valueTemperature?.roundToInt()?.let { "%d°C".format(it) }
            ?: "N/A"

        TemperatureUnitOptions.Fahrenheit -> valueTemperature?.let { ((it * 9 / 5) + 32).roundToInt() }
            ?.let { "%d°F".format(it) } ?: "N/A"

        else -> valueTemperature?.roundToInt()?.let { "%d°C".format(it) }
            ?: "N/A" // Default to Celsius
    }
    return formattedValue
}

data class TemperatureModel(val valueTemperature: Double?) {
    fun formatTemperature(
        unitTemp: TemperatureUnitOptions?,
    ): String {
        val formattedValue = when (unitTemp) {
            TemperatureUnitOptions.Celsius -> valueTemperature?.roundToInt()?.let { "%d°C".format(it) }
                ?: "N/A"

            TemperatureUnitOptions.Fahrenheit -> valueTemperature?.let { ((it * 9 / 5) + 32).roundToInt() }
                ?.let { "%d°F".format(it) } ?: "N/A"

            else -> valueTemperature?.roundToInt()?.let { "%d°C".format(it) }
                ?: "N/A" // Default to Celsius
        }
        return formattedValue
    }
    companion object {
        fun formatTemperatureRange(
            context: Context,
            unitTemp: TemperatureUnitOptions?,
            minTemp: Double?,
            maxTemp: Double?
        ): String {
            val formattedMinTemp = formatSingleTemperature(minTemp, unitTemp)
            val formattedMaxTemp = formatSingleTemperature(maxTemp, unitTemp)
            return context.getString(R.string.temp_weather_day_item, formattedMinTemp, formattedMaxTemp)
        }
    }
}