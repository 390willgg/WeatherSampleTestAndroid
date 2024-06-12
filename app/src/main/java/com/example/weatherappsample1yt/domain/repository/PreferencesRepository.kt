package com.example.weatherappsample1yt.domain.repository

import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveApiPreferences(provider: ApiProviderOptions?)
    suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?)
    suspend fun getApiPreferences(): ApiProviderOptions?
    suspend fun getTemperaturePreferences(): TemperatureUnitOptions?
    fun observeApiPreferences(): Flow<ApiProviderOptions?>
    fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?>

    suspend fun saveCityData(cityData: CityData?)
    suspend fun getCityData(): List<CityData>?
    suspend fun deleteCityData(positionData: Int)
    fun observeCityData(): Flow<List<CityData>?>
}