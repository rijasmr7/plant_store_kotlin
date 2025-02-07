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
                // Use the updated getCityFromCoordinates function
                getCityFromCoordinates(context, location.latitude, location.longitude) { city ->
                    onLocationFetched(city)
                }
            } else {
                // Request real-time GPS location update if last known location is null
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setMinUpdateIntervalMillis(2000)
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        locationResult.locations.firstOrNull()?.let { newLocation ->
                            getCityFromCoordinates(context, newLocation.latitude, newLocation.longitude) { city ->
                                onLocationFetched(city)
                            }
                            fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting location
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        }
    }
}

fun getCityFromCoordinates(context: Context, lat: Double, lon: Double, onCityFetched: (String) -> Unit) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 12+ uses async Geocoder
            geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<android.location.Address>) {
                    val city = addresses.firstOrNull()?.locality ?: "Unknown"
                    onCityFetched(city) // Return the city via callback
                }
            })
        } else {
            // Synchronous method for Android 11 and below
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            val city = addresses?.firstOrNull()?.locality ?: "Unknown"
            onCityFetched(city) // Return city via callback
        }
    } catch (e: Exception) {
        onCityFetched("Unknown") // Handle exceptions safely
    }
}


