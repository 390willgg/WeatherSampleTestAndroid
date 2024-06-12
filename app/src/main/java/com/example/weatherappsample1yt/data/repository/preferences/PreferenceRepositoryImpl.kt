package com.example.weatherappsample1yt.data.repository.preferences

import android.util.Log
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceKey
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private class PreferenceRepositoryImpl(private val dataSource: PreferenceDataSource) :
    PreferencesRepository {
    override suspend fun saveApiPreferences(provider: ApiProviderOptions?) {
        dataSource.savePreference(
            PreferenceKey.API_PROVIDER,
            provider?.name ?: ApiProviderOptions.OPEN_WEATHER.name
        )
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        dataSource.savePreference(
            PreferenceKey.TEMPERATURE_UNIT,
            unit?.name ?: TemperatureUnitOptions.Celsius.name
        )
    }

    override suspend fun getApiPreferences(): ApiProviderOptions {
        val preference = dataSource.getPreference(
            PreferenceKey.API_PROVIDER,
            ApiProviderOptions.OPEN_WEATHER.name
        )
        return ApiProviderOptions.valueOf(preference)
    }

    override suspend fun getTemperaturePreferences(): TemperatureUnitOptions {
        val preference = dataSource.getPreference(
            PreferenceKey.TEMPERATURE_UNIT,
            TemperatureUnitOptions.Celsius.name,
        )
        return TemperatureUnitOptions.valueOf(preference)
    }

    override fun observeApiPreferences(): Flow<ApiProviderOptions?> {
        return dataSource.observePreference(
            PreferenceKey.API_PROVIDER,
            ApiProviderOptions.OPEN_WEATHER.name,
        ).map {
            it?.let { it1 -> ApiProviderOptions.valueOf(it1) }
        }
    }

    override fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?> {
        return dataSource.observePreference(
            PreferenceKey.TEMPERATURE_UNIT,
            TemperatureUnitOptions.Celsius.name,
        ).map {
            it?.let { it1 -> TemperatureUnitOptions.valueOf(it1) }
        }
    }

    override suspend fun saveCityData(cityData: CityData?) {
        withContext(Dispatchers.IO) {
            dataSource.saveCityData(cityData)
        }
    }

    override suspend fun getCityData(): List<CityData>? {
        return dataSource.getCityData()
    }

    override suspend fun deleteCityData(positionData: Int) {
        Log.i("PreferencesRepositoryImpl", "deleteCityData: $positionData")
        dataSource.deleteCityData(positionData)
    }

    override fun observeCityData(): Flow<List<CityData>?> {
        return dataSource.observeCityData()
    }
}

fun getPreferenceRepositoryImpl(dataSource: PreferenceDataSource): PreferencesRepository =
    PreferenceRepositoryImpl(dataSource)