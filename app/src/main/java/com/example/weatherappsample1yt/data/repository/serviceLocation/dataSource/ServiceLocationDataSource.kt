package com.example.weatherappsample1yt.data.repository.serviceLocation.dataSource

import android.app.Activity
import android.location.Location

interface ServiceLocationDataSource {
    suspend fun getLastLocation(activity: Activity): Location?
}