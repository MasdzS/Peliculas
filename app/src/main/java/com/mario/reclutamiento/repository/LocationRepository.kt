package com.mario.reclutamiento.repository

import com.mario.domain.models.LocationPoint
import kotlinx.coroutines.flow.Flow
import mx.com.satoritech.web.NetworkResult

interface LocationRepository {
    fun getUserLocation():Flow<LocationPoint>
    suspend fun getUserLocations():Flow<NetworkResult<List<LocationPoint>>>
    suspend fun saveUserLocation(locationPoint: LocationPoint)
}