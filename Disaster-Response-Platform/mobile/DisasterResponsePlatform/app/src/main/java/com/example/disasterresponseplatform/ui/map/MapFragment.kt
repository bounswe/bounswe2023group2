package com.example.disasterresponseplatform.ui.map


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize the osmdroid library configuration
        Configuration.getInstance().load(context, context?.getSharedPreferences("osmdroid", 0))

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)

        // Set the default zoom level and center of the map
        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(37.7749, -122.4194) // San Francisco coordinates
        mapController.setCenter(startPoint)

        return view
    }
}