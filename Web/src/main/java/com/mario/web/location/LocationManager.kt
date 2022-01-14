package com.mario.web.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.mario.domain.models.LocationPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

private const val TAG = "LocationManager"

/**
 * Obtiene la ubicación del usuario mediante los servicios de google
 */
class LocationManager(
    val context: Context
) {
    private val onLocationUpdate = MutableLiveData<LocationPoint>()
    private val task: Task<LocationSettingsResponse>
    private val fusedLocationClient:FusedLocationProviderClient
    private val locationRequest:LocationRequest

    /**
     * Se configura la solicitud de la ubicación
     */
    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create().apply {
            interval = 1800000//30 minutes
            fastestInterval = 1800000//30 minutes
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(context)
        task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            task.addOnSuccessListener { locationRequest-> Log.d(TAG, "onLocationSuccess: Location success") }
            task.addOnFailureListener { exeption-> Log.e(TAG, "onLocation failed: Location failed")}
        }
    }

    /**
     * Se inician las actualizaciones de la ubicación
     */
    fun startUpdates():Flow<LocationPoint>{
        if(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "startUpdates: Permission denied")
        }else{
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.getMainLooper())
        }
        return onLocationUpdate.asFlow()
    }

    /**
     * Configura los callbacks para obtener la ubicación
     */
    private val locationCallBack = object:LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            GlobalScope.launch {
                onLocationUpdate.postValue(
                    LocationPoint(
                        latitude = locationResult.lastLocation.latitude,
                        longitude = locationResult.lastLocation.longitude
                    )
                )
            }
        }
    }

}