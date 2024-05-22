package com.example.weatherappsample1yt.presentation.view.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Named

class CityViewModel @AssistedInject constructor(
    @Named("OWCity") private val owCitiesListUseCase: CitiesListUseCase,
    @Named("WACity") private val waCitiesListUseCase: CitiesListUseCase,
    @Named("AMSCity") private val amsCitiesListUseCase: CitiesListUseCase,
    @Assisted private val preferencesUseCase : PreferencesUseCase,
): ViewModel() {
    private val _citiesList = MutableLiveData<CityWeatherData?>()
    val citiesList : LiveData<CityWeatherData?> = _citiesList

    @AssistedFactory
    interface Factory {
        fun create(preferencesUseCase : PreferencesUseCase): CityViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory, preferencesUseCase : PreferencesUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(preferencesUseCase) as T
            }
        }
    }

    private suspend fun getCitiesListUseCase(): CitiesListUseCase {
        val apiOptions = preferencesUseCase.getApiPreferences()
        return when (apiOptions) {
            ApiProviderOptions.OPEN_WEATHER -> owCitiesListUseCase
            ApiProviderOptions.AI_METEOSOURCE -> amsCitiesListUseCase
            ApiProviderOptions.WEATHER_API -> waCitiesListUseCase
           else -> throw IllegalArgumentException("Invalid API provider")
        }
    }
    
    fun getCitiesList(cityName: String, limit: Int) {
        viewModelScope.launch {
            val cityData = getCitiesListUseCase().getCitiesList(cityName, limit)
            _citiesList.value = cityData
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}