package com.mario.reclutamiento.ui.directions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mario.domain.models.LocationPoint
import com.mario.reclutamiento.R
import com.mario.reclutamiento.databinding.FragmentDirectionsBinding
import dagger.hilt.android.AndroidEntryPoint
import mx.com.satoritech.web.NetworkResult


/**
 * Muestra el mapa con las direcciones almacenadas del usuario
 */
@AndroidEntryPoint
class DirectionsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var vBind:FragmentDirectionsBinding
    private val viewModel: DirectionsViewModel by viewModels()
    private lateinit var gMap:GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vBind = FragmentDirectionsBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return vBind.root
    }

    /**
     * Se invoca cuando el mapa esta preparado para realizar cambios
     */
    override fun onMapReady(map: GoogleMap) {
        gMap = map
        initUpdates()
    }

    /**
     * Configura la comunicación con el viewModel mediante la cual se obtienen las direcciones y se
     * responde al estado de la solicitud
     */
    private fun initUpdates(){
        viewModel.getLocations()
        viewModel.locations.observe(viewLifecycleOwner){
            vBind.pbLoading.visibility = if(it is NetworkResult.Loading) View.VISIBLE else View.GONE

            if(it is NetworkResult.Error){
                Toast.makeText(context, R.string.err, Toast.LENGTH_SHORT).show()
            }

            if(it is NetworkResult.Success){
                showMarkers(it.data?: listOf())
            }
        }
    }

    /**
     * Pinta marcadores en el mapa representando las ubicaciones almacenadas del usuario
     * @param points puntos con las cordenadas de los marcadores a pintar
     */
    private fun showMarkers(points:List<LocationPoint>){
        var position: LatLng? = null
        gMap.clear()
        points.forEach {
            val position = LatLng(
                it.latitude?:0.0,
                it.longitude?:0.0
            )
            gMap.addMarker(
                MarkerOptions()
                    .position(position)
            )
        }
        points.lastOrNull()?.let {
            val cameraPosition = CameraPosition.builder()
                .target(
                    LatLng(
                    it.latitude?:0.0,
                    it.longitude?:0.0
                    )
                )
                .zoom(15f)
                .build()
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }


}