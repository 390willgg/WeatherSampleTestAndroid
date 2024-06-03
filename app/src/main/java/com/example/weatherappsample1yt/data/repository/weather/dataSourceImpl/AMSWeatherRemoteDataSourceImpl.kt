package com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl

import android.util.Log
import com.example.weatherappsample1yt.data.api.ApiServiceAMS
import com.example.weatherappsample1yt.data.model.aiMeteoSource.currentResponse.toCurrentWeatherData
import com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse.ForecastDailyResponseApiAMS
import com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse.ForecastHourlyResponseApiAMS
import com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse.toDailyDetailsList
import com.example.weatherappsample1yt.data.model.aiMeteoSource.forecastResponse.toHourlyDetailsList
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastDetail
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.data.repository.weather.dataSource.WeatherRemoteDataSource
import com.example.weatherappsample1yt.domain.repository.BaseRepository
import com.example.weatherappsample1yt.domain.repository.getRetrofitService
import okhttp3.OkHttpClient

private class AMSWeatherRemoteDataSourceImpl(
    private val client: OkHttpClient
) : WeatherRemoteDataSource, BaseRepository {
    val baseUrl = "https://ai-weather-by-meteosource.p.rapidapi.com/"
    private val service by lazy {
        return@lazy getRetrofitService(ApiServiceAMS::class.java, baseUrl, client)
    }

    override suspend fun getCurrentWeather(
        lat: Double, lon: Double, units: String
    ): CurrentWeatherData {
        return service?.getCurrentWeatherData(lat, lon, "auto", "en", units)?.body()
            ?.toCurrentWeatherData()
            ?: throw Exception("Service error to get current weather data from AMS")
    }

    override suspend fun getForecast(lat: Double, lon: Double, units: String): ForecastWeatherData {
        var hourlyResponse: ForecastHourlyResponseApiAMS? = null
        var dailyResponse: ForecastDailyResponseApiAMS? = null

        try {
            hourlyResponse = service?.getHourlyWeatherData(lat, lon, "UTC", "en", units)?.body()
        } catch (e: Exception) {
            Log.e("Hourly AMSWeatherRemoteDataSourceImpl", "Error fetching weather data", e)
        }
        try {
            dailyResponse = service?.getDailyWeatherData(lat, lon, "en", units)?.body()
        } catch (e: Exception) {
            Log.e("Daily AMSWeatherRemoteDataSourceImpl", "Error fetching weather data", e)
        }

        val hourlyForecast = hourlyResponse?.toHourlyDetailsList()
            ?: throw Exception("Service error to get hourly forecast weather data from AMS")
        val dailyForecast = dailyResponse?.toDailyDetailsList()
            ?: throw Exception("Service error to get daily forecast weather data from AMS")

        val timezone = hourlyResponse.timezone ?: "UTC"

        return ForecastWeatherData(
            country = null,
            city = null,
            lat = lat,
            lon = lon,
            timezone = timezone,
            forecasts = ForecastDetail(dailyForecast, hourlyForecast)
        )
    }
}

fun getAMSWeatherRemoteDataSourceImpl(client: OkHttpClient): WeatherRemoteDataSource {
    return AMSWeatherRemoteDataSourceImpl(client)
}
