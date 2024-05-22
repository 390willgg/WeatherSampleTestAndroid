package com.example.weatherappsample1yt.data.model.openWeather.forecastResponse

import com.example.weatherappsample1yt.data.model.format.DailyDetail
import com.example.weatherappsample1yt.data.model.format.ForecastDetail
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.data.model.format.HourlyDetail
import com.example.weatherappsample1yt.data.model.format.TemperatureModel
import com.google.gson.annotations.SerializedName

data class ForecastResponseApiOW(
    @SerializedName("city") val city: City?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("cod") val cod: String?,
    @SerializedName("list") val list: List<Data>?,
    @SerializedName("message") val message: Int?
)

fun ForecastResponseApiOW.toForecastWeatherData(): ForecastWeatherData {
    val hourlyDetails = this.list?.map { data ->
        HourlyDetail(
            time = data.dtTxt,
            temp = data.main?.temp?.let { TemperatureModel(it) },
            maxTemp = data.main?.tempMax?.let { TemperatureModel(it) },
            minTemp = data.main?.tempMin?.let { TemperatureModel(it) },
            feelsLike = data.main?.feelsLike,
            condition = data.weather?.firstOrNull()?.description,
            icon = data.weather?.firstOrNull()?.icon,
            pressure = data.main?.pressure?.toDouble(),
            humidity = data.main?.humidity,
            windSpeed = data.wind?.speed,
            windDirection = data.wind?.deg?.toString(),
            description = data.weather?.firstOrNull()?.description,
            precipitation = data.rain?.h,
            uvIndex = null // UV index data is not available in the current API response
        )
    } ?: listOf()

    // Group hourly details by day
    val dailyDetailsMap = hourlyDetails.groupBy { it.time?.substring(0, 10) }

    // Compute daily averages and create DailyDetail instances
    val dailyDetails = dailyDetailsMap.map { (date, hourlyList) ->
        val avgTemp =
            hourlyList.mapNotNull { it.temp?.valueTemperature }.average().takeIf { it.isFinite() }
        val maxTemp = hourlyList.mapNotNull { it.maxTemp?.valueTemperature }.maxOrNull()
            ?.let { TemperatureModel(it) }
        val minTemp = hourlyList.mapNotNull { it.minTemp?.valueTemperature }.minOrNull()
            ?.let { TemperatureModel(it) }
        val avgPressure = hourlyList.mapNotNull { it.pressure }.average().takeIf { it.isFinite() }
        val avgHumidity =
            hourlyList.mapNotNull { it.humidity }.average().takeIf { it.isFinite() }?.toInt()
        val avgWindSpeed = hourlyList.mapNotNull { it.windSpeed }.average().takeIf { it.isFinite() }
        val avgPrecipitation =
            hourlyList.mapNotNull { it.precipitation }.average().takeIf { it.isFinite() }

        DailyDetail(
            date = date,
            temp = avgTemp?.let { TemperatureModel(it) },
            maxTemp = maxTemp,
            minTemp = minTemp,
            condition = hourlyList.firstOrNull()?.condition,
            icon = hourlyList.firstOrNull()?.icon,
            description = hourlyList.firstOrNull()?.description,
            precipitation = avgPrecipitation,
            pressure = avgPressure,
            humidity = avgHumidity,
            windSpeed = avgWindSpeed,
            windDirection = hourlyList.firstOrNull()?.windDirection,
            uvIndex = null // UV index data is not available in the current API response
        )
    }.take(3) // Get only the first 3 daily details
    
    return ForecastWeatherData(
        country = this.city?.country,
        city = this.city?.name,
        lat = this.city?.coord?.lat,
        lon = this.city?.coord?.lon,
        timezone = this.city?.timezone.toString(),
        forecasts = ForecastDetail(dailyDetails = dailyDetails, hourlyDetails = hourlyDetails)
    )
}
