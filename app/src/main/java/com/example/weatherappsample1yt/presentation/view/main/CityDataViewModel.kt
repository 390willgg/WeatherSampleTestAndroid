package com.example.weatherappsample1yt.presentation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.data.model.format.CityData
import com.example.weatherappsample1yt.domain.useCase.preferencesUser.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDataViewModel @Inject constructor(private val preferencesUseCase: PreferencesUseCase) :
    ViewModel() {
    private val _cityData = MediatorLiveData<List<CityData>>()
    val cityData: LiveData<List<CityData>> = _cityData

    private val _removedItemPosition = MutableLiveData<Int>()
    val removedItemPosition: LiveData<Int> = _removedItemPosition

    init {
        viewModelScope.launch(Dispatchers.Main) {
            updateCityData()
        }
    }

    fun addCityData(cityData: CityData?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("CityDataViewModel", "addCityData: $cityData")
            if (cityData != null && !isCityDataExist(cityData = cityData)) {
                preferencesUseCase.saveCityData(cityData)
                updateCityData()
            }
        }
    }

    private suspend fun updateCityData() {
        _cityData.postValue(preferencesUseCase.getCityData() ?: emptyList())
        Log.i("CityDataViewModel", "_cityData: ${_cityData.value}")
        Log.i("CityDataViewModel", "updateCityData: ${cityData.value}")
    }

    fun removeCityData(positionData: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            preferencesUseCase.deleteCityData(positionData)
            _removedItemPosition.value = positionData
            Log.i("CityDataViewModel", "_removedItemPosition: ${_removedItemPosition.value}")
            Log.i("CityDataViewModel", "removedItemPosition: ${removedItemPosition.value}")
            updateCityData()
        }
    }

    private suspend fun isCityDataExist(cityData: CityData): Boolean {
        return preferencesUseCase.isCityDataExist(cityData)
    }

    fun observerCityData(): LiveData<List<CityData>?> {
        return preferencesUseCase.observeCityData().asLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}