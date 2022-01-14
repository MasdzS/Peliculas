package com.mario.reclutamiento.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mario.reclutamiento.R
import com.mario.reclutamiento.repository.LocationRepository
import com.mario.reclutamiento.repository.MoviesRepository
import com.mario.reclutamiento.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.*
import javax.inject.Inject

private const val PACKAGE_NAME:String = "com.mario.reclutamiento.services"
private const val CHANNEL_ID:String="location"
private const val NOTIFICATION_ID:Int = 42
private const val EXTRA_STARTED_FROM_NOTIFICATION:String = PACKAGE_NAME+".started_from_notification"

private const val TAG = "LocationService"

@AndroidEntryPoint
class LocationService : LifecycleService() {

    private val supervisorJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main+supervisorJob)
    private lateinit var notificationManager:NotificationManager
    private lateinit var serviceHandler:Handler
    private val binder = LocalBinder()
    @Inject
    lateinit var locationRepository:LocationRepository

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name = getString(R.string.app_name)
            val nChannel = NotificationChannel(CHANNEL_ID,name,NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(nChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supervisorJob.cancel()
    }

    fun getLocation() = serviceScope.launch(Dispatchers.IO) {
        locationRepository.getUserLocation().collect{
            Log.d(TAG, "getLocation: "+it.latitude+","+it.longitude)
            locationRepository.saveUserLocation(it)
        }
    }



    fun getNotificacion():Notification{
        val intent = Intent(this,LocationService::class.java)
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION,true)
        val servicePendingIntent:PendingIntent = PendingIntent
            .getService(this,0,Intent(this,MainActivity::class.java),0)
        val notification:Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this,CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.updatingLocation))
                .setSmallIcon(R.drawable.ic_gps)
                .setContentIntent(servicePendingIntent)
                .setTicker("Ticker")
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.updatingLocation))
                .setSmallIcon(R.drawable.ic_gps)
                .setContentIntent(servicePendingIntent)
                .setTicker("Ticker")
                .build()
        }
        return notification
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        startForeground(NOTIFICATION_ID,getNotificacion())
        getLocation()
        return binder
    }

    inner class LocalBinder:Binder(){
        fun getService(): LocationService {
            return this@LocationService
        }
    }
}