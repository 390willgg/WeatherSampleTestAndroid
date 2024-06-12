package com.example.weatherappsample1yt.data.repository.preferences.dataSource

import com.example.weatherappsample1yt.data.model.format.CityData
import kotlinx.coroutines.flow.Flow

interface PreferenceDataSource {
    suspend fun <T> getPreference(
        key: PreferenceKey,
        defaultValue: T,
    ): T

    suspend fun <T> savePreference(
        key: PreferenceKey,
        defaultValue: T
    )

    fun <T> observePreference(
        key: PreferenceKey,
        defaultValue: T,
    ): Flow<T?>

    suspend fun saveCityData(cityData: CityData?)
    suspend fun getCityData(): List<CityData>?
    suspend fun deleteCityData(positionData: Int)
    fun observeCityData(): Flow<List<CityData>?>
}

enum class PreferenceKey(val key: String) {
    API_PROVIDER("api_provider"),
    TEMPERATURE_UNIT("temperature_unit"),
    CITY_DATA("city_data")
}