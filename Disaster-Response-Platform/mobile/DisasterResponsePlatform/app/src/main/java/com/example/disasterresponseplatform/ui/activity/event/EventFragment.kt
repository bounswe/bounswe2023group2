package com.example.disasterresponseplatform.ui.activity.event

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.EventAdapter
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.databinding.FragmentEventBinding
import com.example.disasterresponseplatform.databinding.SortAndFilterBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class EventFragment(
    private val eventViewModel: EventViewModel
) : Fragment(),
    OnCoordinatesSelectedListener {
    //TODO it gives an error when directly selected from tab at the beginning, I spent 2 hours to fix it but I couldn't solve, it should be solved
    private lateinit var binding: FragmentEventBinding
    private lateinit var filterBinding: SortAndFilterBinding
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null
    private val mapFragment = ActivityMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater,container,false)
        sendRequest()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()
    }

    /**
     * It refresh the recycler view whenever returned this page (i.e after adding/editing/deleting item)
     */
    override fun onResume() {
        super.onResume()
        sendRequest()
    }

    /**
     * This sends request to get All Events from backend, if it calls with parameters queries it means that we filter the events
     */
    private fun sendRequest(queries: MutableMap<String, String>? = null) {

        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        eventViewModel.getAllEvents(queries)
        eventViewModel.getLiveDataResponse().observe(requireActivity!!){ eventResponse ->
            arrangeRecyclerView(eventResponse.events)
        }
    }

    private fun arrangeRecyclerView(eventList : List<EventBody.EventRequestBody>){
        val recyclerView = binding.recyclerViewEvents
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }
        val adapter = EventAdapter(eventList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            openEventItemFragment(it)
        }
    }

    private fun openEventItemFragment(event: EventBody.EventRequestBody){
        val eventItemFragment = EventItemFragment(eventViewModel,event)
        addFragment(eventItemFragment,"EventItemFragment")
    }

    /**
     * Arrange search view
     */
    private fun arrangeSearchView(){
        searchView = binding.searchView
        searchView.clearFocus() // if anything wrote before delete them
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // when user click submit
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the query submission (e.g., start a search)
                //performSearch(query)
                return true
            }
            // when text is changed on button
            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle changes in the search query (e.g., filter a list)
                //filterList(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            // Handle the search view closing (e.g., clear search results)
            //clearSearchResults()
            false // Return true if you want to consume the event, otherwise return false
        }
    }

    private fun arrangeView(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Handle the refresh action here, e.g., fetch updated data from the backend
            // and update the RecyclerView adapter
            sendRequest()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.btFilter.setOnClickListener {
            showFilterDialog()
        }

        binding.btAddEvent.setOnClickListener {
            addEvent()
        }

        val fab: ExtendedFloatingActionButton = binding.btAddEvent

        binding.recyclerViewEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Extend and shrink the Floating Action Button
                if (dy > 10 && fab.isExtended) { fab.shrink() }
                if (dy < -10 && !fab.isExtended) { fab.extend() }
                if (!recyclerView.canScrollVertically(-1)) { fab.extend() }

            }
        })

        arrangeSearchView()
        //arrangeRecyclerView()
        sendRequest()
    }

    private fun addEvent(){
        val token = DiskStorageManager.getKeyValue("token")
        if ( DiskStorageManager.hasKey("token") && !token.isNullOrEmpty()) {
            val addEventFragment = AddEventFragment(eventViewModel,null)
            addFragment(addEventFragment,"AddEventFragment")
        }
        else{
            Toast.makeText(context, getString(R.string.pr_login_required), Toast.LENGTH_LONG).show()
        }
    }


    /**
     * Arrange filter and sort
     */
    private fun showFilterDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filterBinding = SortAndFilterBinding.inflate(layoutInflater)
        dialog.setContentView(filterBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val locationSwitch = filterBinding.swLocationFilter
        val locationLay = filterBinding.layLocationFilter
        val etXCoordinate = filterBinding.etCoordinateX
        val etYCoordinate = filterBinding.etCoordinateY
        val typeSwitch = filterBinding.swTypeFilter
        val typeLay = filterBinding.layTypeFilter
        val applyButton = filterBinding.btApply
        val cancelButton = filterBinding.btCancel
        val mapButton = filterBinding.btSelectFromMap
        val typesChipGroup = filterBinding.cgTypes

        filterBinding.chUrgency.visibility = View.GONE
        filterBinding.cgSubTypes.visibility = View.GONE
        filterBinding.tvSubType.visibility = View.GONE

        // Add chips from event_types array into Type chip group
        for (eventType in resources.getStringArray(R.array.event_types)) {
            val chip = Chip(requireContext())
            chip.text = eventType
            chip.isCheckable = true
            chip.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            typesChipGroup.addView(chip)
        }

        // Set up Type Filter switch listener
        typeSwitch.setOnClickListener {
            typeLay.visibility = if (typeSwitch.isChecked) View.VISIBLE else View.GONE
        }

        // Set up Location Filter switch listener
        locationSwitch.setOnClickListener {
            locationLay.visibility = if (locationSwitch.isChecked) View.VISIBLE else View.GONE
        }

        trackUserPickLocation()
        // Set up map button click listener
        mapButton.setOnClickListener {
            if (mapButton.isChecked) {
                mapButton.text = getString(R.string.sf_location_select)
                etXCoordinate.text?.clear()
                etYCoordinate.text?.clear()
                mapButton.isChecked = false
            } else {
                mapFragment.isDialog = true // arrange that as a dialog instead of fragment
                mapFragment.coordinatesSelectedListener = this@EventFragment
                mapFragment.show(parentFragmentManager, "mapDialog")
            }
        }

        // Set up apply button click listener
        applyButton.setOnClickListener {
            sendRequest(gatherFilterDetails())
            dialog.dismiss()
        }

        // Set up cancel button click listener
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun gatherFilterDetails(): MutableMap<String, String> {
        val sortByChipGroup = filterBinding.cgSort
        val selectedSortById = filterBinding.cgSort.checkedChipId

        val selectedSortBy: String = when (sortByChipGroup.findViewById<Chip>(selectedSortById)?.text) {
            getString(R.string.sf_creation) -> "created_at"
            getString(R.string.sf_last_update) -> "last_updated_at"
            getString(R.string.sf_reliability) -> "upvote"
            else -> ""
        }

        val selectedOrder = "desc"

        val typeSwitch = filterBinding.swTypeFilter
        val locationSwitch = filterBinding.swLocationFilter

        val typesChipGroup = filterBinding.cgTypes
        val selectedTypeIds = filterBinding.cgTypes.checkedChipIds
        val selectedTypes = mutableListOf<String>()

        val selectedXCoordinate = filterBinding.etCoordinateX.text.toString()
        val selectedYCoordinate = filterBinding.etCoordinateY.text.toString()
        val selectedMaxDistance = filterBinding.slDistance.value.toString()

        for (chipId in selectedTypeIds) {
            val chip = typesChipGroup.findViewById<Chip>(chipId)
            chip?.let { selectedTypes.add(it.text.toString()) }
        }

        val queries = mutableMapOf<String, String>()
        queries["active"] = "true"
        selectedSortBy.let { queries["sort_by"] = it }
        selectedOrder.let { queries["order"] = it }
        selectedTypes.let { selectedTypes ->
            if (typeSwitch.isChecked && selectedTypes.isNotEmpty()) {
                queries["types"] = selectedTypes.joinToString(",")
            }
        }

        if (locationSwitch.isChecked && selectedXCoordinate.isNotBlank() && selectedYCoordinate.isNotBlank()){
            selectedXCoordinate.let { queries["x"] = it }
            selectedYCoordinate.let { queries["y"] = it }
            selectedMaxDistance.let { queries["distance_max"] = it
            }
        }

        return queries
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }

    private fun trackUserPickLocation(){
        mapFragment.getLocationChosen().observe(requireActivity!!){chosen ->
            if (chosen){
                Log.i("LocationMAP","IS CHOSEN")
                parentFragmentManager.setFragmentResultListener(
                    "coordinatesKey",
                    viewLifecycleOwner
                ) { _, bundle ->
                    filterBinding.etCoordinateX.setText(bundle.getDouble("x_coord").toString())
                    filterBinding.etCoordinateY.setText(bundle.getDouble("y_coord").toString())
                    filterBinding.btSelectFromMap.isChecked = true
                    filterBinding.btSelectFromMap.text = getString(R.string.sf_location_selected)
                }
                // to ensure it's not continuously listening the port after the location is received
                parentFragmentManager.clearFragmentResultListener("coordinatesKey")
            }
        }
    }

    override fun onCoordinatesSelected(x: Double, y: Double) {
        //Log.d("YOOO x", x.toString())
        //Log.d("YOOO y", y.toString())
    }
}