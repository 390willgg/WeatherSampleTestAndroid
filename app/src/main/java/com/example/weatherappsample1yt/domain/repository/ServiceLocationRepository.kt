package com.example.weatherappsample1yt.domain.repository

import android.app.Activity
import android.location.Location

interface ServiceLocationRepository {
    suspend fun getLocation(activity: Activity): Location?
}