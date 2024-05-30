package com.example.weatherappsample1yt.domain.useCase.preferencesUser

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

    override suspend fun saveApiPreferences(provider: ApiProviderOptions?) {
        repository.saveApiPreferences(provider)
    }

    override suspend fun saveTemperaturePreferences(unit: TemperatureUnitOptions?) {
        repository.saveTemperaturePreferences(unit)
    }

    override fun bindAppStateFlow(flow: MutableStateFlow<AppState>) {
        CoroutineScope((Dispatchers.IO)).launch {
            observeTemperaturePreferences().collect {
                val newAppState = flow.value.copy(temperatureUnitOptions = it)
                flow.value = newAppState
            }
        }
    }
}

fun getPreferencesUseCaseImpl(repository: PreferencesRepository): PreferencesUseCase =
    PreferencesUseCaseImpl(repository)