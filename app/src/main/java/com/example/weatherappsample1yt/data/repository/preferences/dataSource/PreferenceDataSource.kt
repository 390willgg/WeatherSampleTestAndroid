package com.example.weatherappsample1yt.data.repository.preferences.dataSource

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
}

enum class PreferenceKey(val key: String) {
    API_PROVIDER("api_provider"),
    TEMPERATURE_UNIT("temperature_unit")
}