package com.example.weatherappsample1yt.domain.useCase.serviceLocation

import android.app.Activity
import android.location.Location

interface ServiceLocationUseCase{
    suspend fun getLocation(activity: Activity): Location?
}