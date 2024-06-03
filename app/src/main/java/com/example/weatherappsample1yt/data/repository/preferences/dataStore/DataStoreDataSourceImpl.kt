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
        savePreferenceKey(
            dataStore,
            PreferencesKeys.apiProvider,
            provider,
            ApiProviderOptions.OPEN_WEATHER
        )
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        savePreferenceKey(
            dataStore,
            PreferencesKeys.temperatureUnit,
            unit,
            TemperatureUnitOptions.Celsius
        )
    }

    override suspend fun getApiPreferences(): ApiProviderOptions {
        return getPreferenceKey(
            dataStore,
            PreferencesKeys.apiProvider,
            ApiProviderOptions.OPEN_WEATHER
        ) { name -> ApiProviderOptions.valueOf(name) }
    }

    override suspend fun getTemperaturePreferences(): TemperatureUnitOptions {
        return getPreferenceKey(
            dataStore,
            PreferencesKeys.temperatureUnit,
            TemperatureUnitOptions.Celsius
        ) { name -> TemperatureUnitOptions.valueOf(name) }
    }

    override fun observeApiPreferences(): Flow<ApiProviderOptions?> {
        return observePreferenceKey(
            dataStore,
            PreferencesKeys.apiProvider,
            ApiProviderOptions.OPEN_WEATHER,
            ApiProviderOptions::valueOf
        )
    }

    override fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?> {
        return observePreferenceKey(
            dataStore,
            PreferencesKeys.temperatureUnit,
            TemperatureUnitOptions.Celsius,
            TemperatureUnitOptions::valueOf
        )
    }
}

suspend fun <T> savePreferenceKey(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<String>,
    value: T?,
    default: T
) where T : Enum<T> {
    dataStore.edit { preferences ->
        preferences[key] = value?.name ?: default.name
    }
}

suspend fun <T> getPreferenceKey(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<String>,
    default: T,
    valueOf: (String) -> T
): T {
    val preferences = dataStore.data.first()
    val name = preferences[key] ?: default.toString()
    return valueOf(name)
}

fun <T> observePreferenceKey(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<String>,
    default: T,
    valueOf: (String) -> T
): Flow<T?> {
    return dataStore.data.map { preferences ->
        val name = preferences[key] ?: default.toString()
        valueOf(name)
    }
}

fun getDataStoreDataSourceImpl(dataStore: DataStore<Preferences>): PreferenceDataSource =
    DataStoreDataSourceImpl(dataStore)
