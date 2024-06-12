package com.example.weatherappsample1yt.domain.useCase.preferencesUser

import android.util.Log
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.domain.repository.PreferencesRepository
import com.example.weatherappsample1yt.presentation.AppState
import com.example.weatherappsample1yt.presentation.view.options.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.options.TemperatureUnitOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private class PreferencesUseCaseImpl(private val repository: PreferencesRepository) :
    PreferencesUseCase {
    override suspend fun getApiPreferences(): ApiProviderOptions? {
        return repository.getApiPreferences()
    }

    override suspend fun getTemperaturePreferences(): TemperatureUnitOptions? {
        return repository.getTemperaturePreferences()
    }

    override fun observeApiPreferences(): Flow<ApiProviderOptions?> {
        return repository.observeApiPreferences()
    }

    override fun observeTemperaturePreferences(): Flow<TemperatureUnitOptions?> {
        return repository.observeTemperaturePreferences()
    }

    override suspend fun saveCityData(cityData: CityData?) {
        Log.i("PreferencesUseCaseImpl", "saveCityData: $cityData")
        repository.saveCityData(cityData)
        Log.i("PreferencesUseCaseImpl", "saveCityData: ${repository.getCityData()}")
    }

    override suspend fun getCityData(): List<CityData>? {
        Log.i(
            "PreferencesUseCaseImpl", "getCityData: ${repository.getCityData()}"
        )
        return repository.getCityData()
    }

    override suspend fun deleteCityData(positionData: Int) {
        Log.i("PreferencesUseCaseImpl", "deleteCityData: $positionData")
        repository.deleteCityData(positionData)
    }

    override suspend fun isCityDataExist(cityData: CityData): Boolean {
        Log.i("PreferencesUseCaseImpl", "isCityDataExist: $cityData")
        val existingCityData = getCityData() ?: return false
        return existingCityData.any {
            it.cityName == cityData.cityName
        }
    }

    override fun observeCityData(): Flow<List<CityData>?> {
        return repository.observeCityData()
    }

    override suspend fun saveApiPreferences(provider: ApiProviderOptions?) {
        repository.saveApiPreferences(provider)
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        repository.saveTemperaturePreferences(unit)
    }

    override fun bindAppStateFlow(flow: MutableStateFlow<AppState>) {
        CoroutineScope(Dispatchers.IO).launch {
            observeTemperaturePreferences().collect {
                val newAppState = flow.value.copy(temperatureUnitOptions = it)
                Log.i("PreferencesUseCaseImpl", "bindAppStateFlow: $newAppState")
                flow.value = newAppState
            }
        }
    }
}

fun getPreferencesUseCaseImpl(repository: PreferencesRepository): PreferencesUseCase =
    PreferencesUseCaseImpl(repository)