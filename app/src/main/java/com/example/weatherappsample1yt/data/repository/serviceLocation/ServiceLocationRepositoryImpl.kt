package com.example.weatherappsample1yt.data.repository.serviceLocation

import android.app.Activity
import android.content.Context
import android.location.Location
import com.example.weatherappsample1yt.data.repository.serviceLocation.dataSourceImpl.getServiceLocationDataSourceImpl
import com.example.weatherappsample1yt.domain.repository.ServiceLocationRepository

private class ServiceLocationRepositoryImpl(private val context: Context) :
    ServiceLocationRepository {
    override suspend fun getLocation(activity: Activity): Location? {
        return getServiceLocationDataSourceImpl(context).getLastLocation(activity)
    }
}

fun getServiceLocationRepositoryImpl(context: Context): ServiceLocationRepository {
    return ServiceLocationRepositoryImpl(context)
}