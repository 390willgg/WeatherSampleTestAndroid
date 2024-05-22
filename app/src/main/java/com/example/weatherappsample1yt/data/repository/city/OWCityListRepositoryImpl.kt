package com.example.weatherappsample1yt.data.repository.city

import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.data.repository.city.dataSource.CityListRemoteDataSource
import com.example.weatherappsample1yt.data.repository.city.dataSourceImpl.getOWCityListRemoteDataSourceImpl
import com.example.weatherappsample1yt.domain.repository.CityRepository
import okhttp3.OkHttpClient

private class OWCityListRepositoryImpl: CityRepository {
    private val apiKey = "49cac901dfcef84ab95c1c3d792d9a04"
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.addHeader("Authorization", "Bearer $apiKey")

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    private val cityListRemoteDataSource: CityListRemoteDataSource =
        getOWCityListRemoteDataSourceImpl(client)

    override suspend fun getCitiesList(cityName: String, limit: Int): CityWeatherData? {
        return cityListRemoteDataSource.getCities(cityName, limit)
    }
}

fun getOWCityListRepositoryImpl(): CityRepository {
    return OWCityListRepositoryImpl()
}