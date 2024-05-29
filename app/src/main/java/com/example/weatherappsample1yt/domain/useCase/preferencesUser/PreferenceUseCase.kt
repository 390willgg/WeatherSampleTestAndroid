package com.example.weatherappsample1yt.domain.useCase.preferencesUser

import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow

interface PreferencesUseCase {
    suspend fun saveApiPreferences(provider: ApiProviderOptions?)
    suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?)
    suspend fun getApiPreferences(): ApiProviderOptions?
    suspend fun getTemperaturePreferences(): TemperatureUnitOptions?
    fun observeApiPreferences(): Flow<ApiProviderOptions?>
    fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?>
}