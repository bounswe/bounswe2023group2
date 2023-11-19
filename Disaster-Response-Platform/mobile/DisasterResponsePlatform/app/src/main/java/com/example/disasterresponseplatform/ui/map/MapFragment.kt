package com.example.disasterresponseplatform.ui.map


import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.ResourceBody
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker




class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var mapView: MapView
    private var mapViewModel: MapViewModel = MapViewModel()
    private lateinit var needClusterer: RadiusMarkerClusterer
    private lateinit var resourceClusterer: RadiusMarkerClusterer
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
        mapView.minZoomLevel = 6.0

        // Set the default zoom level and center of the map
        val mapController: IMapController = mapView.controller
        mapController.setZoom(6.0) // A zoom level that shows most of Turkey
        val startPoint = GeoPoint(39.9334, 32.8597)
        mapController.setCenter(startPoint)

        return view
    }
    private fun isNeedValidLocation(needItem: NeedBody.NeedItem): Boolean {
        val lat = needItem.x
        val lon = needItem.y

        // Check if latitude and longitude are not null and within valid range
        return lat != null && lon != null &&
                lat >= -90.0 && lat <= 90.0 &&
                lon >= -180.0 && lon <= 180.0
    }

    private fun isResourceValidLocation(resourceItemItem: ResourceBody.ResourceItem): Boolean {
        val lat = resourceItemItem.x
        val lon = resourceItemItem.y

        // Check if latitude and longitude are not null and within valid range
        return lat != null && lon != null &&
                lat >= -90.0 && lat <= 90.0 &&
                lon >= -180.0 && lon <= 180.0
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.sendGetAllNeedRequest()
        mapViewModel.sendGetAllResourceRequest()
        needClusterer = RadiusMarkerClusterer(context)
        resourceClusterer = RadiusMarkerClusterer(context)
        mapViewModel.getLiveDataNeedResponse().observe(viewLifecycleOwner) { needItems ->
            needItems.needs.forEach { needItem ->
                addNeedMarker(needItem)
            }
            mapView.overlays.add(needClusterer)
        }
        mapViewModel.getLiveDataResourceResponse().observe(viewLifecycleOwner) { resourceItems ->
            resourceItems.resources.forEach {resourceItem ->
                addResourceMarker(resourceItem)
            }
            mapView.overlays.add(needClusterer)
        }
    }

    private fun addNeedMarker(needItem: NeedBody.NeedItem) {
        if (isNeedValidLocation(needItem)) {
            val point = GeoPoint(needItem.x!!.toDouble(), needItem.y!!.toDouble())
            val marker = Marker(mapView)
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = needItem.getDescription()
            marker.subDescription = needItem.getSubDescription()
            needClusterer.add(marker)
        }
    }

    private fun addResourceMarker(resourceItem: ResourceBody.ResourceItem) {
        if (isResourceValidLocation(resourceItem)) {
            val point = GeoPoint(resourceItem.x!!.toDouble(), resourceItem.y!!.toDouble())
            val marker = Marker(mapView)
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = resourceItem.getDescription()

            // Apply a color filter to the default marker
            val defaultDrawable = marker.icon.mutate() // Get and mutate the default icon
            defaultDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN)
            marker.setIcon(defaultDrawable)
            needClusterer.add(marker)
        }
    }
}