package com.example.disasterresponseplatform.ui.activity.util.map

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.disasterresponseplatform.databinding.ActivityMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

lateinit var mLocationManager: LocationManager

interface OnCoordinatesSelectedListener {
    fun onCoordinatesSelected(x: Double, y: Double)
}

class ActivityMap : Fragment() {

    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: MapView
    private lateinit var marker: Marker
    private var selectedPoint: GeoPoint? = null
    private lateinit var userLocationMarker: Marker
    var coordinatesSelectedListener: OnCoordinatesSelectedListener? = null

    private fun someMethodWhereYouGetCoordinates(x: Double, y: Double) {
        coordinatesSelectedListener?.onCoordinatesSelected(x, y)

        // Optionally pop the Map Fragment from the stack to go back to Add Fragment
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    inner class TouchOverlay : Overlay() {
        override fun onTouchEvent(event: MotionEvent, mapView: MapView): Boolean {
            if (event.action == MotionEvent.ACTION_UP) {
                selectedPoint = mapView.mapCenter as GeoPoint
                marker.position = selectedPoint!!
                mapView.invalidate() // Refresh the map to show the updated marker position
            }
            return false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMapWithLocation()
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeMapWithLocation()
                }
            }
        }
    }

    private fun initializeMapWithLocation() {
        println("initialize map with location")

        try {
            val lastKnownLocation = getLastKnownLocation()
            val zoom: Double
            if (lastKnownLocation != null) {
                selectedPoint = GeoPoint(lastKnownLocation.latitude, lastKnownLocation.longitude)
                zoom = 18.5
            } else {
                selectedPoint = GeoPoint(41.086105, 29.0440387) // Eiffel Tower
                zoom = 9.5
            }
            setUpMapView(zoom)

        } catch (_: SecurityException) {
        }
    }

    private fun setUpMapView(zoom: Double) {
        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(zoom)
        mapController.setCenter(selectedPoint)

        marker = Marker(map)
        marker.position = selectedPoint!!
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map.overlays.add(marker)

        val touchOverlay = TouchOverlay()
        map.overlays.add(touchOverlay)

        map.addMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                updateMarkerPosition()
                return false
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                return false
            }
        })

    }

    private fun updateMarkerPosition() {
        selectedPoint = map.mapCenter as GeoPoint
        marker.position = selectedPoint
        map.invalidate() // Refresh the map to update marker position
        val result = bundleOf("x_coord" to marker.position.latitude, "y_coord" to marker.position.longitude)
        coordinatesSelectedListener?.onCoordinatesSelected(marker.position.latitude, marker.position.longitude)
        setFragmentResult("coordinatesKey", result)
    }


    override fun onResume() {
        super.onResume()
        try {
            map.onResume()
        } catch (_: Exception) {}

    }

    override fun onPause() {
        super.onPause()
        try {
        map.onPause()
        } catch (_: Exception) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLastKnownLocation(): Location? {
        try {
            mLocationManager =
                requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
            val providers: List<String> = mLocationManager.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l: Location = mLocationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
            return bestLocation
        } catch (e: SecurityException) {
            println("Security exception")
            return null
        }
    }
}
