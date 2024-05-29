package com.example.weatherappsample1yt.domain.useCase.serviceLocation

import android.app.Activity
import android.location.Location
import com.example.weatherappsample1yt.domain.repository.ServiceLocationRepository

private class ServiceLocationUseCaseImpl(private val repository: ServiceLocationRepository) :
    ServiceLocationUseCase {
    override suspend fun getLocation(activity: Activity): Location? {
        return repository.getLocation(activity)
    }
}

fun getServiceLocationUseCaseImpl(repository: ServiceLocationRepository): ServiceLocationUseCase {
    return ServiceLocationUseCaseImpl(repository)
}