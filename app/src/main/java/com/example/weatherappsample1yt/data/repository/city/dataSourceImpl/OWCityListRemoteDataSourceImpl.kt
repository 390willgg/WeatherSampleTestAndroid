package com.example.weatherappsample1yt.data.repository.city.dataSourceImpl

import android.util.Log
import com.example.weatherappsample1yt.data.api.ApiServiceOW
import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.data.model.openWeather.toCityWeatherData
import com.example.weatherappsample1yt.data.repository.city.dataSource.CityListRemoteDataSource
import com.example.weatherappsample1yt.domain.repository.BaseRepository
import com.example.weatherappsample1yt.domain.repository.getRetrofitService
import okhttp3.OkHttpClient

private class OWCityListRemoteDataSourceImpl(
    client: OkHttpClient,
) : CityListRemoteDataSource, BaseRepository {
    private val service by lazy {
        return@lazy getRetrofitService(
            ApiServiceOW::class.java,
            "https://api.openweathermap.org/",
            client
        )
    }

    override suspend fun getCities(cityName: String, limit: Int): CityWeatherData? {
        val response = service?.getCitiesList(
            cityName,
            limit,
            "49cac901dfcef84ab95c1c3d792d9a04"
        )
        Log.i(
            "OWCityListRemoteDataSourceImpl", "getCities: response = $response?.toCityWeatherData()"
        )
        return response?.toCityWeatherData()
    }
}

fun getOWCityListRemoteDataSourceImpl(client: OkHttpClient): CityListRemoteDataSource {
    return OWCityListRemoteDataSourceImpl(client)
}