package com.example.weatherappsample1yt.data.repository.serviceLocation.dataSourceImpl

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.weatherappsample1yt.data.repository.serviceLocation.dataSource.ServiceLocationDataSource
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private class ServiceLocationDataSourceImpl @Inject constructor(private val context : Context) :
	ServiceLocationDataSource {

	private val fusedLocationClient : FusedLocationProviderClient =
		LocationServices.getFusedLocationProviderClient(context)

	@OptIn(DelicateCoroutinesApi::class)
	private val gpsEnabledReceiver = object : BroadcastReceiver() {
		override fun onReceive(context : Context?, intent : Intent?) {
			if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
				if (isGPSEnabled()) {
					// GPS has been enabled, request location update here
					if (context is Activity) {
						GlobalScope.launch(Dispatchers.Main) {
							getLastLocation(context)
						}
					}
				}
			}
		}
	}

	private fun createLocationRequest() : LocationRequest {
		return LocationRequest.Builder(
			Priority.PRIORITY_HIGH_ACCURACY,
			10000,
		).build()
	}

	private val locationCallback : LocationCallback = object : LocationCallback() {
		override fun onLocationResult(locationResult : LocationResult) {
			super.onLocationResult(locationResult)
			for (location in locationResult.locations) {
				Log.d(
					"ServiceLocationDataSource",
					"Location: ${location.latitude}, ${location.longitude}"
				)
			}
		}
	}

	private fun checkLocationSettings(activity : Activity) {
		val locationRequest = createLocationRequest()
		val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
		val client : SettingsClient = LocationServices.getSettingsClient(activity)
		val tasks : Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

		tasks.addOnSuccessListener {
			if (ContextCompat.checkSelfPermission(
					context, Manifest.permission.ACCESS_FINE_LOCATION
				) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
					context, Manifest.permission.ACCESS_COARSE_LOCATION
				) == PackageManager.PERMISSION_GRANTED
			) {
				fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
			}
		}

		tasks.addOnFailureListener { exception ->
			if (exception is ResolvableApiException) {
				try {
					exception.startResolutionForResult(
						activity, REQUEST_CHECK_SETTINGS
					)
				}
				catch (sendEx : IntentSender.SendIntentException) {
					// Ignore the error.
				}
			} else {
				Log.e("ServiceLocationDataSource", exception.message ?: "")
			}
		}
	}

	init {
		val intentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
		context.registerReceiver(gpsEnabledReceiver, intentFilter)
	}

	override suspend fun getLastLocation(activity : Activity) : Location? {
		if (!isGPSEnabled()) {
			checkLocationSettings(activity)
			return null
		}

		if (ContextCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_FINE_LOCATION
			) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
				context, Manifest.permission.ACCESS_COARSE_LOCATION
			) != PackageManager.PERMISSION_GRANTED
		) {
			return null
		}
		val lastLocation = try {
			fusedLocationClient.lastLocation.await()
		}
		catch (e : SecurityException) {
			Log.e("ServiceLocationDataSource", e.message ?: "")
			null
		}
		return lastLocation ?: getCurrentLocation()
	}

	private fun isGPSEnabled() : Boolean {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
	}

	private suspend fun getCurrentLocation() : Location? {
		val cancellationTokenSource = CancellationTokenSource()
		return try {
			fusedLocationClient.getCurrentLocation(
				Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token
			).await()
		}
		catch (e : SecurityException) {
			Log.e("ServiceLocationDataSource", e.message ?: "")
			null
		}
	}

	companion object {
		private const val REQUEST_CHECK_SETTINGS = 0x1
	}
}

fun getServiceLocationDataSourceImpl(context : Context) : ServiceLocationDataSource =
	ServiceLocationDataSourceImpl(context)