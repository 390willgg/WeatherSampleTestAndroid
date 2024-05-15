package com.example.weatherappsample1yt.data.repository.preferences

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.data.repository.preferences.dataStore.getDataStoreDataSourceImpl
import com.example.weatherappsample1yt.data.repository.preferences.sharePrefereces.getSharedPreferencesDataSourceImpl
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import kotlinx.coroutines.flow.Flow


private class PreferenceRepositoryImpl(private val dataSource : PreferenceDataSource) : PreferencesRepository {
	override suspend fun saveApiPreferences(provider : ApiProviderOptions?) {
		dataSource.saveApiPreferences(provider)
	}
	
	override suspend fun saveTemperaturePreferences(unit : TemperatureUnitOptions?) {
		dataSource.saveTemperaturePreferences(unit)
	}
	
	override suspend fun getApiPreferences() : ApiProviderOptions? {
		return dataSource.getApiPreferences()
	}
	
	override suspend fun getTemperaturePreferences() : TemperatureUnitOptions? {
		return dataSource.getTemperaturePreferences()
	}
	
	override suspend fun observeApiPreferences() : Flow<ApiProviderOptions?> {
		return dataSource.observeApiPreferences()
	}
	
	override suspend fun observeTemperaturePreferences() : Flow<TemperatureUnitOptions?> {
		return dataSource.observeTemperaturePreferences()
	}
}

fun getPreferenceRepositoryImpl(dataSource : PreferenceDataSource) : PreferencesRepository =
	PreferenceRepositoryImpl(dataSource)