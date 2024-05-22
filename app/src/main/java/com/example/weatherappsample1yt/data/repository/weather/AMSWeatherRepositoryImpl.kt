package com.example.weatherappsample1yt.data.repository.weather

import android.util.Log
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.data.repository.weather.dataSource.WeatherRemoteDataSource
import com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl.getAMSWeatherRemoteDataSourceImpl
import com.example.weatherappsample1yt.domain.repository.WeatherRepository
import okhttp3.OkHttpClient

private class AMSWeatherRepositoryImpl : WeatherRepository {
    private val apiKey = "f21ca166c8msh5a96ef3a0168171p118214jsn85d65948bad2"
    private val client = OkHttpClient.Builder().addInterceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.addHeader("X-RapidAPI-Key", apiKey)
        requestBuilder.addHeader(
            "X-RapidAPI-Host", "ai-weather-by-meteosource.p.rapidapi.com"
        )
        val request = requestBuilder.build()
        it.proceed(request)
    }.build()

    val weatherRemoteDataSource: WeatherRemoteDataSource = getAMSWeatherRemoteDataSourceImpl(client)

    override suspend fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        units: String
    ): CurrentWeatherData? {
        Log.d("Repository", "Using AMSWeatherRepository for getCurrentWeatherData")
        return weatherRemoteDataSource.getCurrentWeather(lat, lon, units)
    }

    override suspend fun getForecastWeatherData(
        lat: Double,
        lon: Double,
        units: String
    ): ForecastWeatherData? {
        Log.d("Repository", "Using AMSWeatherRepository for getForecastWeatherData")
        return weatherRemoteDataSource.getForecast(lat, lon, units)
    }

}

fun getAMSWeatherRepositoryImpl(): WeatherRepository  {
    Log.d("Repository", "Creating AMSWeatherRepository")
    return AMSWeatherRepositoryImpl()
}
