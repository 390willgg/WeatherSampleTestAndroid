package com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse

import com.example.weatherappsample1yt.data.model.format.HourlyDetail
import com.example.weatherappsample1yt.data.model.format.TemperatureModel
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class ForecastHourlyResponseApiAMS(
    @SerializedName("elevation") val elevation: Int?,
    @SerializedName("hourly") val hourly: Hourly?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("units") val units: String?
)

fun convertDateFormat(inputDateString: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    return try {
        val date = inputFormat.parse(inputDateString)
        date?.let { outputFormat.format(it) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ForecastHourlyResponseApiAMS.toHourlyDetailsList(): List<HourlyDetail> {
    return this.hourly?.data?.mapNotNull { data ->
        data?.let {
            HourlyDetail(
                time = it.date?.let { it1 -> convertDateFormat(it1) },
                temp = TemperatureModel(it.temperature),
                feelsLike = it.feelsLike,
                condition = it.weather,
                icon = it.icon?.toString() ?: "default", // Adjusted to handle null icon
                description = it.summary,
                uvIndex = it.uvIndex,
                maxTemp = null, // Placeholder, consider providing a mechanism to fill this
                minTemp = null, // Placeholder, consider providing a mechanism to fill this
                windSpeed = it.wind?.speed,
                windDirection = it.wind?.angle?.toString() ?: "N/A", // Adjusted to handle null wind direction
                pressure = it.pressure,
                humidity = it.humidity,
                precipitation = it.precipitation?.total
            )
        }
    }.orEmpty() // Ensures a non-null list is returned
}