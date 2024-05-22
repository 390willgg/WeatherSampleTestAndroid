package com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse

import com.example.weatherappsample1yt.data.model.format.DailyDetail
import com.example.weatherappsample1yt.data.model.format.TemperatureModel
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class ForecastDailyResponseApiAMS(
    @SerializedName("daily") val daily: Daily?,
    @SerializedName("elevation") val elevation: Int?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?,
    @SerializedName("units") val units: String?
) {
    data class Daily(
        @SerializedName("data") val `data`: List<Data?>?
    ) {
        data class Data(
            @SerializedName("cloud_cover") val cloudCover: Int?,
            @SerializedName("day") val day: String?,
            @SerializedName("dew_point") val dewPoint: Double?,
            @SerializedName("dew_point_max") val dewPointMax: Double?,
            @SerializedName("dew_point_min") val dewPointMin: Double?,
            @SerializedName("feels_like") val feelsLike: Double?,
            @SerializedName("feels_like_max") val feelsLikeMax: Double?,
            @SerializedName("feels_like_min") val feelsLikeMin: Double?,
            @SerializedName("humidity") val humidity: Int?,
            @SerializedName("icon") val icon: Int?,
            @SerializedName("ozone") val ozone: Double?,
            @SerializedName("precipitation") val precipitation: Precipitation?,
            @SerializedName("predictability") val predictability: Int?,
            @SerializedName("pressure") val pressure: Double?,
            @SerializedName("probability") val probability: Probability?,
            @SerializedName("summary") val summary: String?,
            @SerializedName("temperature") val temperature: Double?,
            @SerializedName("temperature_max") val temperatureMax: Double?,
            @SerializedName("temperature_min") val temperatureMin: Double?,
            @SerializedName("visibility") val visibility: Double?,
            @SerializedName("weather") val weather: String?,
            @SerializedName("wind") val wind: Wind?,
            @SerializedName("wind_chill") val windChill: Double?,
            @SerializedName("wind_chill_max") val windChillMax: Double?,
            @SerializedName("wind_chill_min") val windChillMin: Double?
        ) {
            data class Precipitation(
                @SerializedName("total") val total: Double?,
                @SerializedName("type") val type: String?
            )

            data class Probability(
                @SerializedName("freeze") val freeze: Int?,
                @SerializedName("precipitation") val precipitation: Int?,
                @SerializedName("storm") val storm: Int?
            )

            data class Wind(
                @SerializedName("angle") val angle: Int?,
                @SerializedName("dir") val dir: String?,
                @SerializedName("gusts") val gusts: Double?,
                @SerializedName("speed") val speed: Double?
            )
        }
    }
}

private fun parseDate(dateString: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    return try {
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) }
    } catch (e: Exception) {
        null
    }
}

fun ForecastDailyResponseApiAMS.toDailyDetailsList(): List<DailyDetail>? {
    return this.daily?.data?.take(3)?.mapNotNull { dailyData -> // Take only the first 3 elements
        dailyData?.let {
            DailyDetail(
                date = it.day?.let { it1 -> parseDate(it1) },
                temp = TemperatureModel(it.temperature),
                maxTemp = TemperatureModel(it.feelsLikeMax),
                minTemp = TemperatureModel(it.feelsLikeMin),
                condition = it.summary,
                icon = it.icon.toString(),
                description = it.summary,
                precipitation = it.precipitation?.total,
                pressure = it.pressure,
                humidity = it.humidity,
                windSpeed = it.windChill,
                windDirection = it.wind?.angle.toString(),
                uvIndex = null
            )
        }
    }
}