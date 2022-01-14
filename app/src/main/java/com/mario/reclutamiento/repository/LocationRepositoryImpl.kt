package com.mario.reclutamiento.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mario.domain.models.LocationPoint
import com.mario.web.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import mx.com.satoritech.web.NetworkResult
import javax.inject.Inject

private const val TAG = "LocationRepositoryImp"

/**
 * Se encarga de consultar a firestore y la api de servicios de google para obtener y almacenar
 * la ubicación del usuario
 */
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
) : LocationRepository {

    val locationManager = LocationManager(context)
    val firestore = Firebase.firestore

    /**
     * Obtiene la ubicación del usuario mediante los servicios de google
     * @return Un flujo mediante el cual se recibira la ubicación del usuario
     */
    override fun getUserLocation(): Flow<LocationPoint> {
        return locationManager.startUpdates()
    }

    /**
     * Obtiene las ubicaciones del usuario almacenadas en firestore
     * @return Un flujo mediante el cual se obtienen las ubicaciones asi como el
     * estado de la petición
     */
    suspend override fun getUserLocations(): Flow<NetworkResult<List<LocationPoint>>> {
        val locationPoints = MutableLiveData<NetworkResult<List<LocationPoint>>>()
        val points = mutableListOf<LocationPoint>()
        val userId  = Firebase.analytics.firebaseInstanceId
        locationPoints.postValue(NetworkResult.Loading())
        firestore.collection("users").document(userId).collection("directions").get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val data = document.getData()
                    points.add(
                        LocationPoint(
                            latitude = data["latitude"].toString().toDouble(),
                            longitude = data.get("longitude").toString().toDouble()
                        )
                    )
                }
                locationPoints.postValue(NetworkResult.Success(points))
            }
            .addOnFailureListener {
                locationPoints.postValue(NetworkResult.Error(it.message?:""))
                Log.d(TAG, "getUserLocations: "+it.message)
            }
        return locationPoints.asFlow()
    }

    /**
     * Almacena la ubicacion del usuario en firebase
     * @param locationPoint objeto que contiene la longitud y latitud de la ubicación
     */
    suspend override fun saveUserLocation(locationPoint: LocationPoint){
        val userId  = Firebase.analytics.firebaseInstanceId
        val location = hashMapOf(
            "latitude" to locationPoint.latitude,
            "longitude" to locationPoint.longitude
        )
        firestore.collection("users").document(userId).collection("directions").add(location)
    }
}