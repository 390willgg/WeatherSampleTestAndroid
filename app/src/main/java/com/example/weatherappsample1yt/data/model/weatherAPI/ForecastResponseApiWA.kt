package com.example.weatherappsample1yt.data.model.weatherAPI


import com.example.weatherappsample1yt.data.model.format.DailyDetail
import com.example.weatherappsample1yt.data.model.format.ForecastDetail
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.data.model.format.HourlyDetail
import com.example.weatherappsample1yt.data.model.format.TemperatureModel
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class ForecastResponseApiWA(
    @SerializedName("current")
    val current: Current?,
    @SerializedName("forecast")
    val forecast: Forecast?,
    @SerializedName("location")
    val location: Location?
) {
    data class Current(
        @SerializedName("cloud")
        val cloud: Int?,
        @SerializedName("condition")
        val condition: Condition?,
        @SerializedName("feelslike_c")
        val feelslikeC: Double?,
        @SerializedName("feelslike_f")
        val feelslikeF: Double?,
        @SerializedName("gust_kph")
        val gustKph: Double?,
        @SerializedName("gust_mph")
        val gustMph: Double?,
        @SerializedName("humidity")
        val humidity: Int?,
        @SerializedName("is_day")
        val isDay: Int?,
        @SerializedName("last_updated")
        val lastUpdated: String?,
        @SerializedName("last_updated_epoch")
        val lastUpdatedEpoch: Int?,
        @SerializedName("precip_in")
        val precipIn: Double?,
        @SerializedName("precip_mm")
        val precipMm: Double?,
        @SerializedName("pressure_in")
        val pressureIn: Double?,
        @SerializedName("pressure_mb")
        val pressureMb: Int?,
        @SerializedName("temp_c")
        val tempC: Int?,
        @SerializedName("temp_f")
        val tempF: Double?,
        @SerializedName("uv")
        val uv: Int?,
        @SerializedName("vis_km") val visKm: Double?,
        @SerializedName("vis_miles")
        val visMiles: Int?,
        @SerializedName("wind_degree")
        val windDegree: Int?,
        @SerializedName("wind_dir")
        val windDir: String?,
        @SerializedName("wind_kph")
        val windKph: Double?,
        @SerializedName("wind_mph")
        val windMph: Double?
    ) {
        data class Condition(
            @SerializedName("code")
            val code: Int?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("text")
            val text: String?
        )
    }

    data class Forecast(
        @SerializedName("forecastday")
        val forecastday: List<Forecastday?>?
    ) {
        data class Forecastday(
            @SerializedName("astro")
            val astro: Astro?,
            @SerializedName("date")
            val date: String?,
            @SerializedName("date_epoch")
            val dateEpoch: Int?,
            @SerializedName("day")
            val day: Day?,
            @SerializedName("hour")
            val hour: List<Hour?>?
        ) {
            data class Astro(
                @SerializedName("is_moon_up")
                val isMoonUp: Int?,
                @SerializedName("is_sun_up")
                val isSunUp: Int?,
                @SerializedName("moon_illumination")
                val moonIllumination: Int?,
                @SerializedName("moon_phase")
                val moonPhase: String?,
                @SerializedName("moonrise")
                val moonrise: String?,
                @SerializedName("moonset")
                val moonset: String?,
                @SerializedName("sunrise")
                val sunrise: String?,
                @SerializedName("sunset")
                val sunset: String?
            )

            data class Day(
                @SerializedName("avghumidity")
                val avghumidity: Int?,
                @SerializedName("avgtemp_c")
                val avgtempC: Double?,
                @SerializedName("avgtemp_f")
                val avgtempF: Double?,
                @SerializedName("avgvis_km")
                val avgvisKm: Double?,
                @SerializedName("avgvis_miles")
                val avgvisMiles: Int?,
                @SerializedName("condition")
                val condition: Condition?,
                @SerializedName("daily_chance_of_rain")
                val dailyChanceOfRain: Int?,
                @SerializedName("daily_chance_of_snow")
                val dailyChanceOfSnow: Int?,
                @SerializedName("daily_will_it_rain")
                val dailyWillItRain: Int?,
                @SerializedName("daily_will_it_snow")
                val dailyWillItSnow: Int?,
                @SerializedName("maxtemp_c")
                val maxtempC: Double?,
                @SerializedName("maxtemp_f")
                val maxtempF: Double?,
                @SerializedName("maxwind_kph")
                val maxwindKph: Double?,
                @SerializedName("maxwind_mph")
                val maxwindMph: Double?,
                @SerializedName("mintemp_c")
                val mintempC: Double?,
                @SerializedName("mintemp_f")
                val mintempF: Double?,
                @SerializedName("totalprecip_in")
                val totalprecipIn: Double?,
                @SerializedName("totalprecip_mm")
                val totalprecipMm: Double?,
                @SerializedName("totalsnow_cm")
                val totalsnowCm: Int?,
                @SerializedName("uv")
                val uv: Int?
            ) {
                data class Condition(
                    @SerializedName("code")
                    val code: Int?,
                    @SerializedName("icon")
                    val icon: String?,
                    @SerializedName("text")
                    val text: String?
                )
            }

            data class Hour(
                @SerializedName("chance_of_rain")
                val chanceOfRain: Int?,
                @SerializedName("chance_of_snow")
                val chanceOfSnow: Int?,
                @SerializedName("cloud")
                val cloud: Int?,
                @SerializedName("condition")
                val condition: Condition?,
                @SerializedName("dewpoint_c")
                val dewpointC: Double?,
                @SerializedName("dewpoint_f")
                val dewpointF: Double?,
                @SerializedName("feelslike_c")
                val feelslikeC: Double?,
                @SerializedName("feelslike_f")
                val feelslikeF: Double?,
                @SerializedName("gust_kph")
                val gustKph: Double?,
                @SerializedName("gust_mph")
                val gustMph: Double?,
                @SerializedName("heatindex_c")
                val heatindexC: Double?,
                @SerializedName("heatindex_f")
                val heatindexF: Double?,
                @SerializedName("humidity")
                val humidity: Int?,
                @SerializedName("is_day")
                val isDay: Int?,
                @SerializedName("precip_in")
                val precipIn: Double?,
                @SerializedName("precip_mm")
                val precipMm: Double?,
                @SerializedName("pressure_in")
                val pressureIn: Double?,
                @SerializedName("pressure_mb")
                val pressureMb: Int?,
                @SerializedName("snow_cm")
                val snowCm: Int?,
                @SerializedName("temp_c")
                val tempC: Double?,
                @SerializedName("temp_f")
                val tempF: Double?,
                @SerializedName("time")
                val time: String?,
                @SerializedName("time_epoch")
                val timeEpoch: Int?,
                @SerializedName("uv")
                val uv: Int?,
                @SerializedName("vis_km") val visKm: Double?,
                @SerializedName("vis_miles")
                val visMiles: Int?,
                @SerializedName("will_it_rain")
                val willItRain: Int?,
                @SerializedName("will_it_snow")
                val willItSnow: Int?,
                @SerializedName("wind_degree")
                val windDegree: Int?,
                @SerializedName("wind_dir")
                val windDir: String?,
                @SerializedName("wind_kph")
                val windKph: Double?,
                @SerializedName("wind_mph")
                val windMph: Double?,
                @SerializedName("windchill_c")
                val windchillC: Double?,
                @SerializedName("windchill_f")
                val windchillF: Double?
            ) {
                data class Condition(
                    @SerializedName("code")
                    val code: Int?,
                    @SerializedName("icon")
                    val icon: String?,
                    @SerializedName("text")
                    val text: String?
                )
            }
        }
    }

    data class Location(
        @SerializedName("country")
        val country: String?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("localtime")
        val localtime: String?,
        @SerializedName("localtime_epoch")
        val localtimeEpoch: Int?,
        @SerializedName("lon")
        val lon: Double?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("region")
        val region: String?,
        @SerializedName("tz_id")
        val tzId: String?
    )

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

private fun parseDateToFullFormat(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date!!)
}

fun ForecastResponseApiWA.toForecastWeatherData(): ForecastWeatherData {
    val dailyDetails = this.forecast?.forecastday?.mapNotNull { forecastDay ->
        forecastDay?.day?.let {
            DailyDetail(
                date = forecastDay.date?.let { it1 -> parseDateToFullFormat(it1) }, // Assuming date is in "yyyy-MM-dd" format
                maxTemp = TemperatureModel(forecastDay.day.maxtempC),
                minTemp = TemperatureModel(forecastDay.day.mintempC),
                condition = forecastDay.day.condition?.text,
                precipitation = forecastDay.day.totalprecipMm,
                pressure = null,
                humidity = null,
                windSpeed = null,
                windDirection = null,
                uvIndex = forecastDay.day.uv?.toDouble(),
                icon = forecastDay.day.condition?.icon,
                description = forecastDay.day.condition?.text,
                temp = TemperatureModel(forecastDay.day.avgtempC)
            )
        }
    }?.toList()

    val hourlyDetails = this.forecast?.forecastday?.flatMap { forecastDay ->
        forecastDay?.hour?.mapNotNull { hour ->
            HourlyDetail(
                time = hour?.time?.let { parseDate(it).toString() }, // Assuming time is in "yyyy-MM-dd HH:mm:ss" format
                temp = TemperatureModel(hour?.tempC),
                feelsLike = hour?.feelslikeC,
                condition = hour?.condition?.text,
                precipitation = hour?.precipMm,
                windSpeed = hour?.windKph,
                windDirection = hour?.windDir,
                icon = hour?.condition?.icon,
                description = hour?.condition?.text,
                pressure = hour?.pressureMb?.toDouble(),
                humidity = hour?.humidity,
                uvIndex = hour?.uv?.toDouble(),
                maxTemp = TemperatureModel(hour?.dewpointC),
                minTemp = TemperatureModel(hour?.heatindexC)
            )
        } ?: listOf()
    } ?: listOf()

    return ForecastWeatherData(
        country = this.location?.country,
        city = this.location?.name,
        lat = this.location?.lat,
        lon = this.location?.lon,
        timezone = this.location?.tzId,
        forecasts = ForecastDetail(dailyDetails = dailyDetails, hourlyDetails = hourlyDetails)
    )
}