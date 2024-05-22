package com.example.weatherappsample1yt.presentation.view.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import com.example.weatherappsample1yt.presentation.view.main.ApiProviderOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CityViewModel @Inject constructor(
    @Named("OWCity") private val owCitiesListUseCase: CitiesListUseCase,
    @Named("WACity") private val waCitiesListUseCase: CitiesListUseCase,
    @Named("AMSCity") private val amsCitiesListUseCase: CitiesListUseCase,
): ViewModel() {
    @Inject
    lateinit var preferencesUseCase: PreferencesUseCase

    private val _citiesList = MutableLiveData<CityWeatherData?>()
    val citiesList : LiveData<CityWeatherData?> = _citiesList

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