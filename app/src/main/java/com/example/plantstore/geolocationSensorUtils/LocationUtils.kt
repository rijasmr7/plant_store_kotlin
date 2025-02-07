package com.example.plantstore.geolocationSensorUtils


import android.content.Context
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.*
import java.util.*

fun getUserLocation(context: Context, onLocationFetched: (String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userCity = getCityFromCoordinates(context, location.latitude, location.longitude)
                onLocationFetched(userCity)
            } else {
                // If lastLocation is null, request a new location update
                val locationRequest = LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 5000
                    fastestInterval = 2000
                }
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.locations.firstOrNull()?.let { newLocation ->
                            val city = getCityFromCoordinates(context, newLocation.latitude, newLocation.longitude)
                            onLocationFetched(city)
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        }
    }
}

fun getCityFromCoordinates(context: Context, lat: Double, lon: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addressList = geocoder.getFromLocation(lat, lon, 1)
        addressList?.firstOrNull()?.locality ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}
