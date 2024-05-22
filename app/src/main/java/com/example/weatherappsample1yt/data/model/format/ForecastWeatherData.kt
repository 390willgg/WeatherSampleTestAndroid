package com.example.weatherappsample1yt.data.model.format

data class ForecastWeatherData(
    val country: String? = null,
    val city: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val timezone: String? = null,
    val forecasts: ForecastDetail? = null
)

data class ForecastDetail(
    val dailyDetails: List<DailyDetail>? = emptyList(),
    val hourlyDetails: List<HourlyDetail>? = emptyList()
)

data class DailyDetail(
    val date: String? = null,
    val temp: TemperatureModel? = null,
    val maxTemp: TemperatureModel? = null,
    val minTemp: TemperatureModel? = null,
    val condition: String? = null,
    val icon: String? = null,
    val description: String? = null,
    val precipitation: Double? = null,
    val pressure: Double? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
    val windDirection: String? = null,
    val uvIndex: Double? = null
)

data class HourlyDetail(
    val time: String? = null,
    val temp: TemperatureModel? = null,
    val maxTemp: TemperatureModel? = null,
    val minTemp: TemperatureModel? = null,
    val feelsLike: Double? = null,
    val condition: String? = null,
    val icon: String? = null,
    val pressure: Double? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
    val windDirection: String? = null,
    val description: String? = null,
    val precipitation: Double? = null,
    val uvIndex: Double? = null
)


