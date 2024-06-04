package com.example.weatherappsample1yt.data.repository.preferences.sharePrefereces

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
private class SharedPreferencesDataSourceImpl(context: Context) :
    PreferenceDataSource {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    override suspend fun <T> savePreference(key: PreferenceKey, defaultValue: T) {
        val preferenceKey = key.key
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                putString(preferenceKey, defaultValue.toString())
            }
        }
    }

    override suspend fun <T> getPreference(
        key: PreferenceKey,
        defaultValue: T,
    ): T {
        val preferenceKey = key.key
        val preferenceValue = sharedPreferences.getString(preferenceKey, defaultValue.toString())
        return preferenceValue as T
    }

    override fun <T> observePreference(
        key: PreferenceKey,
        defaultValue: T,
    ): Flow<T?> {
        val preferenceKey = key.key
        val flow = MutableStateFlow<T?>(null)
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == preferenceKey) {
                val preferenceValue =
                    sharedPreferences.getString(changedKey, defaultValue.toString())
                flow.value = preferenceValue as T
            }
        }
        return flow
    }
}

fun getSharedPreferencesDataSourceImpl(context: Context): PreferenceDataSource =
    SharedPreferencesDataSourceImpl(context)