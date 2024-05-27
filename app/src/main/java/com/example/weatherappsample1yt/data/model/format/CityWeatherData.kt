package com.example.weatherappsample1yt.data.model.format

import androidx.compose.runtime.Immutable

@Immutable
data class CityWeatherData(
    val data: ArrayList<DataItemCity>? = null
)

@Immutable
data class DataItemCity(
    val cityName: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeId: String? = null,
    val timezone: String? = null,
    val region: String? = null,
    val idCity: Int? = null,
    val typeArea: String? = null,
    val localNames: Map<String, String?>? = null
)