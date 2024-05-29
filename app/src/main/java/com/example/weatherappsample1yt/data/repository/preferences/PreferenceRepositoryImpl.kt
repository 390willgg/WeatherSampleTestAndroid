package com.example.weatherappsample1yt.data.repository.preferences

import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow


private class PreferenceRepositoryImpl(private val dataSource: PreferenceDataSource) :
    PreferencesRepository {
    override suspend fun saveApiPreferences(provider: ApiProviderOptions?) {
        dataSource.saveApiPreferences(provider)
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        dataSource.saveTemperaturePreferences(unit)
    }

    override suspend fun getApiPreferences(): ApiProviderOptions? {
        return dataSource.getApiPreferences()
    }

    override suspend fun getTemperaturePreferences(): TemperatureUnitOptions? {
        return dataSource.getTemperaturePreferences()
    }

    override fun observeApiPreferences(): Flow<ApiProviderOptions?> {
        return dataSource.observeApiPreferences()
    }

    override fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?> {
        return dataSource.observeTemperaturePreferences()
    }
}

fun getPreferenceRepositoryImpl(dataSource: PreferenceDataSource): PreferencesRepository =
    PreferenceRepositoryImpl(dataSource)