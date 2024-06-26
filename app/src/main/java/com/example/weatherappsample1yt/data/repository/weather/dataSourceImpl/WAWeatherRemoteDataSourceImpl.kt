package com.example.weatherappsample1yt.data.repository.weather.dataSourceImpl

import android.util.Log
import com.example.weatherappsample1yt.data.api.ApiServiceWA
import com.example.weatherappsample1yt.data.model.format.CurrentWeatherData
import com.example.weatherappsample1yt.data.model.format.ForecastWeatherData
import com.example.weatherappsample1yt.data.model.weatherAPI.CurrentResponseApiWA
import com.example.weatherappsample1yt.data.model.weatherAPI.ForecastResponseApiWA
import com.example.weatherappsample1yt.data.model.weatherAPI.toForecastWeatherData
import com.example.weatherappsample1yt.data.repository.weather.dataSource.WeatherRemoteDataSource
import com.example.weatherappsample1yt.domain.repository.BaseRepository
import com.example.weatherappsample1yt.domain.repository.getRetrofitService
import okhttp3.OkHttpClient

private class WAWeatherRemoteDataSourceImpl(
    private val client: OkHttpClient
) : WeatherRemoteDataSource, BaseRepository {
    private val baseUrl = "https://weatherapi-com.p.rapidapi.com/"
    private val service by lazy {
        return@lazy getRetrofitService(ApiServiceWA::class.java, baseUrl, client)
    }
    
    override suspend fun getCurrentWeather(
        lat : Double, lon : Double, units : String,
    ) : CurrentWeatherData? {
        val location = "$lat,$lon"
        var response : CurrentResponseApiWA? = null
        try {
            response = service?.getCurrentWeatherData(location)
        }
        catch (e : Exception) {
            Log.i("WAWeatherRemoteDataSourceImpl", "getCurrentWeather: ${e.message}")
        }
        return response?.toCurrentWeatherData()
    }
    
    override suspend fun getForecast(
        lat : Double, lon : Double, units : String,
    ) : ForecastWeatherData? {
        val location = "$lat,$lon"
        var response : ForecastResponseApiWA? = null
        try {
            response = service?.getForecastWeatherData(location, 3)
            Log.i("WAWeatherRemoteDataSourceImpl", "getForecast: response = $response")
        }
        catch (e : Exception) {
            Log.i("WAWeatherRemoteDataSourceImpl", "getForecast: ${e.message}")
        }
        return response?.toForecastWeatherData()
    }
}

fun getWAWeatherRemoteDataSourceImpl(client: OkHttpClient): WeatherRemoteDataSource {
    return WAWeatherRemoteDataSourceImpl(client)
}