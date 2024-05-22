package com.example.weatherappsample1yt.data.repository.city

import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.data.repository.city.dataSource.CityListRemoteDataSource
import com.example.weatherappsample1yt.data.repository.city.dataSourceImpl.getAMSCityListRemoteDataSourceImpl
import com.example.weatherappsample1yt.domain.repository.CityRepository
import okhttp3.OkHttpClient

private class AMSCityListRepositoryImpl: CityRepository {
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

    val cityListRemoteDataSource: CityListRemoteDataSource = getAMSCityListRemoteDataSourceImpl(client)
    override suspend fun getCitiesList(cityName: String, limit: Int): CityWeatherData? {
        return cityListRemoteDataSource.getCities(cityName, limit)
    }
}

fun getAMSCityListRepositoryImpl(): CityRepository {
    return AMSCityListRepositoryImpl()
}