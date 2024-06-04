package com.example.weatherappsample1yt.data.repository.preferences.dataStore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceKey
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Suppress("UNCHECKED_CAST")
private class DataStoreDataSourceImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceDataSource {
    override suspend fun <T> savePreference(
        key: PreferenceKey,
        defaultValue: T
    ) {
        val preferenceKey = getPreferenceKey(key, defaultValue)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = defaultValue
        }
    }

    override suspend fun <T> getPreference(
        key: PreferenceKey,
        defaultValue: T,
    ): T {
        val preferenceKey = getPreferenceKey(key, defaultValue)

        val preferences = dataStore.data.first()
        val preferencesValue = preferences[preferenceKey] ?: defaultValue
        return try {
            preferencesValue
        } catch (e: IllegalArgumentException) {
            Log.i("DataStoreDataSourceImpl", "Error parsing preferences value", e)
            defaultValue
        }
    }

    override fun <T> observePreference(
        key: PreferenceKey,
        defaultValue: T,
    ): Flow<T?> {
        val preferenceKey = getPreferenceKey(key, defaultValue)
        return dataStore.data.map { preferences ->
            val preferencesValue = preferences[preferenceKey] ?: defaultValue
            try {
                preferencesValue
            } catch (e: IllegalArgumentException) {
                Log.i("DataStoreDataSourceImpl", "Error parsing preferences value", e)
                defaultValue
            }
        }
    }

    fun <T> getPreferenceKey(key: PreferenceKey, defaultValue: T): Preferences.Key<T> {
        return when (defaultValue) {
            is String -> stringPreferencesKey(key.key) as Preferences.Key<T>
            is Int -> intPreferencesKey(key.key) as Preferences.Key<T>
            is Float -> floatPreferencesKey(key.key) as Preferences.Key<T>
            is Long -> longPreferencesKey(key.key) as Preferences.Key<T>
            is Boolean -> booleanPreferencesKey(key.key) as Preferences.Key<T>
            is Set<*> -> stringSetPreferencesKey(key.key) as Preferences.Key<T>
            is ByteArray -> byteArrayPreferencesKey(key.key) as Preferences.Key<T>
            is Double -> floatPreferencesKey(key.key) as Preferences.Key<T>
            is TemperatureUnitOptions,
            is ApiProviderOptions -> stringPreferencesKey(key.key) as Preferences.Key<T>

            else -> {
                throw IllegalArgumentException("Type not supported")
            }
        }
    }
}

fun getDataStoreDataSourceImpl(dataStore: DataStore<Preferences>): PreferenceDataSource =
    DataStoreDataSourceImpl(dataStore)
