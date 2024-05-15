package com.example.weatherappsample1yt.domain.useCase.serviceLocation

import android.app.Activity
import android.location.Location
import com.example.weatherappsample1yt.domain.repository.ServiceLocationRepository
import javax.inject.Inject

private class ServiceLocationUseCaseImpl @Inject constructor(private val repository: ServiceLocationRepository): ServiceLocationUseCase{
    override suspend fun getLocation(activity: Activity): Location? {
        return repository.getLocation(activity)
    }
}

fun getServiceLocationUseCaseImpl(repository: ServiceLocationRepository): ServiceLocationUseCase{
    return ServiceLocationUseCaseImpl(repository)
}