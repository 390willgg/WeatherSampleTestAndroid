package com.example.weatherappsample1yt.data.repository.preferences.sharePrefereces

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.weatherappsample1yt.data.repository.preferences.dataSource.PreferenceDataSource
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private class SharedPreferencesDataSourceImpl @Inject constructor(context : Context) :
	PreferenceDataSource {
	private val sharedPreferences : SharedPreferences =
		context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
	
	private val _apiProviderFlow = MutableStateFlow<ApiProviderOptions?>(null)
	private val apiProviderFlow : Flow<ApiProviderOptions?> = _apiProviderFlow
	
	private val _temperatureUnitFlow = MutableStateFlow<TemperatureUnitOptions?>(null)
	private val temperatureUnitFlow : Flow<TemperatureUnitOptions?> = _temperatureUnitFlow
	
	init {
		loadInitialPreferences()
	}
	
	private fun loadInitialPreferences() {
		// This function is called in the init block to initialize the flows
		CoroutineScope(Dispatchers.IO).launch {
			_apiProviderFlow.value = getApiPreferences()
			_temperatureUnitFlow.value = getTemperaturePreferences()
		}
	}
	
	override suspend fun saveApiPreferences(provider : ApiProviderOptions?) {
		withContext(Dispatchers.IO) {
			sharedPreferences.edit {
				putString("api_provider", provider?.name)
			}
			_apiProviderFlow.value = provider
		}
	}
	
	override suspend fun saveTemperaturePreferences(unit : TemperatureUnitOptions?) {
		withContext(Dispatchers.IO) {
			sharedPreferences.edit {
				putString("temperature_unit", unit?.name)
			}
			_temperatureUnitFlow.value = unit
		}
	}
	
	override suspend fun getApiPreferences() : ApiProviderOptions? = withContext(Dispatchers.IO) {
		sharedPreferences.getString("api_provider", null)?.let {
			ApiProviderOptions.valueOf(it)
		}
	}
	
	override suspend fun getTemperaturePreferences() : TemperatureUnitOptions? =
		withContext(Dispatchers.IO) {
			sharedPreferences.getString("temperature_unit", null)?.let {
				TemperatureUnitOptions.valueOf(it)
			}
		}
	
	override suspend fun observeApiPreferences() : Flow<ApiProviderOptions?> = apiProviderFlow
	
	override suspend fun observeTemperaturePreferences() : Flow<TemperatureUnitOptions?> =
		temperatureUnitFlow
}

fun getSharedPreferencesDataSourceImpl(context : Context) : PreferenceDataSource =
	SharedPreferencesDataSourceImpl(context)