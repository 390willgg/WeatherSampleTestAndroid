package com.example.weatherappsample1yt.presentation.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class SettingViewModel @AssistedInject constructor(
    @Assisted private val preferencesUseCase: PreferencesUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(preferencesUseCase: PreferencesUseCase): SettingViewModel
    }

    companion object {
        fun provideFactory(
            factory: Factory,
            preferencesUseCase: PreferencesUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(preferencesUseCase) as T
            }
        }
    }

    fun updateApiProvider(apiProvider: ApiProviderOptions) {
        viewModelScope.launch {
            preferencesUseCase.saveApiPreferences(apiProvider)
        }
    }

    fun loadApiProvider() {
        viewModelScope.launch {
            preferencesUseCase.getApiPreferences()
        }
    }

    fun updateTemperatureUnit(unit: TemperatureUnitOptions) {
        viewModelScope.launch {
            preferencesUseCase.saveTemperaturePreferences(unit)
        }
    }

    fun loadTemperatureUnit() {
        viewModelScope.launch {
            preferencesUseCase.getTemperaturePreferences()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}