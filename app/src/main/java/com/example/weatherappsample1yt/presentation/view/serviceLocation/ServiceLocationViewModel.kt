package com.example.weatherappsample1yt.presentation.view.serviceLocation


import android.app.Activity
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappsample1yt.domain.useCase.serviceLocation.ServiceLocationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceLocationViewModel @Inject constructor(private val useCase: ServiceLocationUseCase) : ViewModel() {

    private val _location = MutableLiveData<Location?>()
    val location: MutableLiveData<Location?> = _location

    fun getLocation(activity: Activity) {
        viewModelScope.launch {
            val result = useCase.getLocation(activity)
            _location.postValue(result)
        }
    }
}