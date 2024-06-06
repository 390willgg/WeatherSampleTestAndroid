package com.example.weatherappsample1yt.presentation.view.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CityWeatherData
import com.example.weatherappsample1yt.domain.useCase.city.CitiesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val citiesListUseCase: CitiesListUseCase
) : ViewModel() {
    private val _citiesList = MutableStateFlow<CityWeatherData?>(null)
    val citiesList: LiveData<CityWeatherData?> = _citiesList.asLiveData()

    fun getCitiesList(cityName: String, limit: Int) {
        viewModelScope.launch {
            val cityData = citiesListUseCase.getCitiesList(cityName, limit)
            _citiesList.value = cityData
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}