package com.example.disasterresponseplatform.ui.map


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyItemFragment
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyViewModel
import com.example.disasterresponseplatform.ui.activity.event.EventItemFragment
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedItemFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceItemFragment
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
import com.example.disasterresponseplatform.utils.UserRoleUtil
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class MapFragment(
    var needViewModel: NeedViewModel, var resourceViewModel: ResourceViewModel,
    var actionViewModel: ActionViewModel, var eventViewModel: EventViewModel,
    var emergencyViewModel: EmergencyViewModel) : Fragment(R.layout.fragment_map) {

    private lateinit var mapView: MapView
    private lateinit var mapViewModel: MapViewModel
    private lateinit var needClusterer: RadiusMarkerClusterer
    private lateinit var allClusters: RadiusMarkerClusterer
    private val networkManager = NetworkManager()

    private var requireActivity: FragmentActivity? = null

    private lateinit var resourceClusterer: RadiusMarkerClusterer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.primary)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)

        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize the osmdroid library configuration
        Configuration.getInstance().load(context, context?.getSharedPreferences("osmdroid", 0))

        // Initialize the MapView
        mapView = view.findViewById(R.id.mapView)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.minZoomLevel = 6.0
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

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

    private fun showNeedsOnMap(){
        mapViewModel.sendGetAllNeedRequest()
        mapViewModel.getLiveDataNeedResponse().observe(requireActivity!!) { needItems ->
            needItems?.needs?.forEach { needItem ->
                try {
                    if (!needClusterer.items.any { marker -> marker.id == needItem._id }
                    // UnComment them if you only want to see today's needs
                    //&& needItem.occur_at?.startsWith(currentDate) != false
                    //&& needItem.active
                    ) {
                        addNeedMarker(needItem)
                        mapView.postInvalidate()
                    }
                    val marker = Marker(mapView)
                    marker.id = needItem._id
                    val point = GeoPoint(needItem.x, needItem.y)
                    marker.position = point
                    allClusters.add(marker)
                    mapView.postInvalidate()
                    mapView.invalidate()
                } catch (e: Exception) {
                    Log.d("hata in need", e.toString())
                }
            }
            mapView.overlays.add(needClusterer)
            mapView.invalidate()
            mapView.postInvalidate()
        }
    }

    private fun showResourcesOnMap(){
        mapViewModel.sendGetAllResourceRequest()
        mapViewModel.getLiveDataResourceResponse().observe(requireActivity!!) { resourceItems ->
            resourceItems?.resources?.forEach {resourceItem ->
                try {
                    if (!needClusterer.items.any { marker -> marker.id == resourceItem._id }
                    // UnComment them if you only want to see today's resources
                    //&& resourceItem.occur_at?.startsWith(currentDate) != false
                    //&& resourceItem.active
                    ) {
                        addResourceMarker(resourceItem)
                        mapView.postInvalidate()
                    }
                    val marker = Marker(mapView)
                    marker.id = resourceItem._id
                    val point = GeoPoint(resourceItem.x, resourceItem.y)
                    marker.position = point
                    allClusters.add(marker)
                    mapView.postInvalidate()
                    mapView.invalidate()
                } catch (e: Exception) {
                    Log.d("hata in resource", e.toString())
                }
            }
            //mapView.overlays.add(needClusterer)
            mapView.overlays.add(needClusterer)
            mapView.invalidate()
            mapView.postInvalidate()
        }
    }

    private fun showEventsOnMap(){
        mapViewModel.sendGetAllEventRequest()
        mapViewModel.getLiveDataEventResponse().observe(requireActivity!!) { eventItems ->
            eventItems?.events?.forEach {eventItem ->
                try {
                    if (!needClusterer.items.any { marker -> marker.id == eventItem._id }
                    // UnComment them if you only want to see today's resources
                    //&& resourceItem.occur_at?.startsWith(currentDate) != false
                    //&& resourceItem.active
                    ) {
                        addEventMarker(eventItem)
                        mapView.postInvalidate()
                    }
                    val marker = Marker(mapView)
                    marker.id = eventItem._id
                    val point = GeoPoint(eventItem.x, eventItem.y)
                    marker.position = point
                    allClusters.add(marker)
                    mapView.postInvalidate()
                    mapView.invalidate()
                } catch (e: Exception) {
                    Log.d("hata in event", e.toString())
                }
            }
            //mapView.overlays.add(needClusterer)
            mapView.overlays.add(needClusterer)
            mapView.invalidate()
            mapView.postInvalidate()
        }
    }

    private fun showEmergenciesOnMap(){
        mapViewModel.sendGetAllEmergenciesRequest()
        mapViewModel.getLiveDataEmergencyResponse().observe(requireActivity!!) { emergencyItems ->
            emergencyItems?.emergencies?.forEach {emergencyItem ->
                try {
                    if (!needClusterer.items.any { marker -> marker.id == emergencyItem._id }
                    // UnComment them if you only want to see today's resources
                    //&& resourceItem.occur_at?.startsWith(currentDate) != false
                    //&& resourceItem.active
                    ) {
                        addEmergencyMarker(emergencyItem)
                        mapView.postInvalidate()
                    }
                    val marker = Marker(mapView)
                    marker.id = emergencyItem._id
                    val point = GeoPoint(emergencyItem.x, emergencyItem.y)
                    marker.position = point
                    allClusters.add(marker)
                    mapView.postInvalidate()
                    mapView.invalidate()
                } catch (e: Exception) {
                    Log.d("hata in emergency", e.toString())
                }
            }
            //mapView.overlays.add(needClusterer)
            mapView.overlays.add(needClusterer)
            mapView.invalidate()
            mapView.postInvalidate()
        }
    }

    private fun showActionsOnMap(){
        mapViewModel.sendGetAllActionsRequest()
        mapViewModel.getLiveDataActionsResponse().observe(requireActivity!!) { actions ->
            actions?.actions?.forEach { actionItem ->
                actionItem.relatedGroups.forEach { group ->
                    group.relatedNeeds.forEach { needId ->
                        group.relatedResources.forEach { resourceId ->
                            drawLineBetweenMarkers(needId, resourceId)
                            mapView.postInvalidate()
                            mapView.invalidate()
                        }
                    }
                }
            }
            mapView.postInvalidate()
            mapView.invalidate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null){
            requireActivity = requireActivity()
        }
        needClusterer = RadiusMarkerClusterer(context)
        resourceClusterer = RadiusMarkerClusterer(context)
        allClusters = RadiusMarkerClusterer(context)
        needClusterer.items.clear()
        showNeedsOnMap()
        showResourcesOnMap()
        showEventsOnMap()
        showEmergenciesOnMap()
        //showActionsOnMap()
    }

    private fun drawLineBetweenMarkers(markerId1: String, markerId2: String) {
        val marker1 = findMarkerById(markerId1)
        val marker2 = findMarkerById(markerId2)

        if (marker1 != null && marker2 != null) {
            Log.d("yess","no ERRORRR")
            val line = Polyline() // Create a new Polyline
            line.setColor(Color.RED) // Set the color of the line
            line.setWidth(4.0f) // Set the width of the line

            val geoPoints = arrayListOf(marker1.position, marker2.position)
            line.setPoints(geoPoints) // Set the points of the line

            mapView.overlays.add(line) // Add the line to the MapView
            mapView.postInvalidate()
            mapView.invalidate()
        } else {

        }
    }

    private fun findMarkerById(markerId: String): Marker? {
        // Assuming needClusterer and resourceClusterer contain all markers
        val allMarkers = allClusters.items

        return allMarkers.firstOrNull { marker -> marker.id == markerId }
    }

    override fun onPause() {
        super.onPause()
        // Save current map state
        mapViewModel.saveMapState(mapView.zoomLevel, mapView.mapCenter)
    }

    override fun onResume() {
        super.onResume()
        // Restore map state if available
        val savedState: MapState? = mapViewModel.getMapState()
        val mapController: IMapController = mapView.controller
        if (savedState != null) {
            mapController.setZoom(savedState.zoomLevel)
            mapController.setCenter(savedState.centerPoint)
        }
    }

    private fun addNeedMarker(needItem: NeedBody.NeedItem) {
        if (isNeedValidLocation(needItem)) {
            val point = GeoPoint(needItem.x, needItem.y)
            val marker = Marker(mapView)
            marker.setInfoWindow(BubbleInfoView(mapView, View.OnClickListener {
                addFragment(NeedItemFragment(needViewModel, needItem),"NeedItemFragment")
            }))
            marker.id = needItem._id
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = needItem.type
            marker.subDescription = needItem.details["subtype"]
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.need_map_icon)

            UserRoleUtil.isCredibleNonBlocking(needItem.created_by) {
                if (it) {
                    marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.credible_need_map_icon)
                }
            }

            needClusterer.add(marker)
        }
    }


    private fun addResourceMarker(resourceItem: ResourceBody.ResourceItem) {
        if (isResourceValidLocation(resourceItem)) {
            val point = GeoPoint(resourceItem.x.toDouble(), resourceItem.y.toDouble())
            val marker = Marker(mapView)
            marker.setInfoWindow(BubbleInfoView(mapView) {
                addFragment(ResourceItemFragment(resourceViewModel, resourceItem),"ResourceItemFragment")
            })
            marker.id = resourceItem._id
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = resourceItem.type
            marker.subDescription = resourceItem.details["subtype"]

            // Apply a color filter to the default marker
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.resource_map_icon)
//            val defaultDrawable = marker.icon.mutate() // Get and mutate the default icon
//            defaultDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN)
//            marker.icon = defaultDrawable

            UserRoleUtil.isCredibleNonBlocking(resourceItem.created_by) {
                if (it) {
                    marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.credible_resource_map_icon)
                }
            }

            needClusterer.add(marker)
        }
    }

    private fun addEventMarker(eventItem: EventBody.EventRequestBody) {
            val point = GeoPoint(eventItem.x.toDouble(), eventItem.y.toDouble())
            val marker = Marker(mapView)
            marker.setInfoWindow(BubbleInfoView(mapView) {
                addFragment(EventItemFragment(eventViewModel, eventItem),"ResourceItemFragment")
            })
            marker.id = eventItem._id
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = eventItem.event_type
            marker.subDescription = eventItem.short_description

            // Apply a color filter to the default marker
            marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.event_map_icon)
//            val defaultDrawable = marker.icon.mutate() // Get and mutate the default icon
//            defaultDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN)
//            marker.icon = defaultDrawable

            UserRoleUtil.isCredibleNonBlocking(eventItem.created_by_user) {
                if (it) {
                    marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.credible_event_map_icon)
                }
            }

            needClusterer.add(marker)
    }

    private fun addEmergencyMarker(emergencyItem: EmergencyBody.EmergencyItem) {
        val point = GeoPoint(emergencyItem.x.toDouble(), emergencyItem.y.toDouble())
        val marker = Marker(mapView)
        marker.setInfoWindow(BubbleInfoView(mapView) {
            addFragment(EmergencyItemFragment(emergencyViewModel, emergencyItem),"ResourceItemFragment")
        })
        marker.id = emergencyItem._id
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = emergencyItem.type
        marker.subDescription = emergencyItem.description

        // Apply a color filter to the default marker
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.emergency_map_icon)
//            val defaultDrawable = marker.icon.mutate() // Get and mutate the default icon
//            defaultDrawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN)
//            marker.icon = defaultDrawable

        if (emergencyItem.created_by != null)
        UserRoleUtil.isCredibleNonBlocking(emergencyItem.created_by) {
            if (it) {
                marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.credible_emergency_map_icon)
            }
        }

        needClusterer.add(marker)
    }

    private fun addFragment(fragment: Fragment,fragmentName: String) {
        val ft = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}