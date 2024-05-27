package com.example.weatherappsample1yt.presentation.view.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import com.example.weatherappsample1yt.presentation.view.main.TemperatureUnitOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var preferencesUseCase: PreferencesUseCase

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