package com.example.weatherappsample1yt.data.repository.preferences.dataSource

import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow

interface PreferenceDataSource {
	suspend fun saveApiPreferences(provider : ApiProviderOptions?)
	suspend fun saveTemperaturePreferences(unit : TemperatureUnitOptions?)
	suspend fun getApiPreferences() : ApiProviderOptions?
	suspend fun getTemperaturePreferences() : TemperatureUnitOptions?
	suspend fun observeApiPreferences() : Flow<ApiProviderOptions?>
	suspend fun observeTemperaturePreferences() : Flow<TemperatureUnitOptions?>
}