package com.example.weatherappsample1yt.presentation.view.serviceLocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappsample1yt.domain.useCase.serviceLocation.ServiceLocationUseCase

@Suppress("UNCHECKED_CAST")
class ServiceLocationViewModelFactory(private val useCase: ServiceLocationUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServiceLocationViewModel(useCase) as T
    }
}