package com.example.weatherappsample1yt.domain.useCase.preferencesUser

import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.presentation.AppState
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface PreferencesUseCase {
    suspend fun saveApiPreferences(provider: ApiProviderOptions?)
    suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?)
    suspend fun getApiPreferences(): ApiProviderOptions?
    suspend fun getTemperaturePreferences(): TemperatureUnitOptions?
    fun bindAppStateFlow(flow: MutableStateFlow<AppState>)
    fun observeApiPreferences(): Flow<ApiProviderOptions?>
    fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?>

    suspend fun saveCityData(cityData: CityData?)
    suspend fun getCityData(): List<CityData>?
    suspend fun deleteCityData(positionData: Int)
    suspend fun isCityDataExist(cityData: CityData): Boolean
    fun observeCityData(): Flow<List<CityData>?>
}