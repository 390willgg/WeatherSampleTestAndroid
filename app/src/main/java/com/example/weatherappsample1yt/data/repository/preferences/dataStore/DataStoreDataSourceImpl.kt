package com.example.weatherappsample1yt.data.repository.preferences.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private class DataStoreDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceDataSource {
    private object PreferencesKeys {
        val apiProvider = stringPreferencesKey("api_provider")
        val temperatureUnit = stringPreferencesKey("temperature_unit")
    }

    override suspend fun saveApiPreferences(provider: ApiProviderOptions?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.apiProvider] =
                provider?.name ?: ApiProviderOptions.OPEN_WEATHER.name
        }
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.temperatureUnit] =
                unit?.name ?: TemperatureUnitOptions.Celsius.name
        }
    }

    override suspend fun getApiPreferences(): ApiProviderOptions {
        val preferences = dataStore.data.first()
        val providerName =
            preferences[PreferencesKeys.apiProvider] ?: ApiProviderOptions.OPEN_WEATHER.name
        return ApiProviderOptions.valueOf(providerName)
    }

    override suspend fun getTemperaturePreferences(): TemperatureUnitOptions {
        val preferences = dataStore.data.first()
        val unitName =
            preferences[PreferencesKeys.temperatureUnit] ?: TemperatureUnitOptions.Celsius.name
        return TemperatureUnitOptions.valueOf(unitName)
    }

    override fun observeApiPreferences(): Flow<ApiProviderOptions?> {
        return dataStore.data.map {
            val providerName =
                it[PreferencesKeys.apiProvider] ?: ApiProviderOptions.OPEN_WEATHER.name
            ApiProviderOptions.valueOf(providerName)
        }
    }

    override fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?> {
        return dataStore.data.map {
            val unitName =
                it[PreferencesKeys.temperatureUnit] ?: TemperatureUnitOptions.Celsius.name
            TemperatureUnitOptions.valueOf(unitName)
        }
    }
}

fun getDataStoreDataSourceImpl(dataStore: DataStore<Preferences>): PreferenceDataSource =
    DataStoreDataSourceImpl(dataStore)
