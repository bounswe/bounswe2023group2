package com.example.disasterresponseplatform.ui.activity.emergency

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.EmergencyAdapter
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.databinding.FragmentEmergencyBinding
import com.example.disasterresponseplatform.databinding.SortAndFilterBinding
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class EmergencyFragment(
    private val emergencyViewModel: EmergencyViewModel
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentEmergencyBinding
    private lateinit var filterBinding: SortAndFilterBinding
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null
    private val mapFragment = ActivityMap()
    private lateinit var emergencyList: List<EmergencyBody.EmergencyItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmergencyBinding.inflate(inflater,container,false)
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

        binding.btAddEmergency.setOnClickListener {
            addEmergency()
        }

        val fab: ExtendedFloatingActionButton = binding.btAddEmergency

        binding.recyclerViewEmergencies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Extend and shrink the Floating Action Button
                if (dy > 10 && fab.isExtended) { fab.shrink() }
                if (dy < -10 && !fab.isExtended) { fab.extend() }
                if (!recyclerView.canScrollVertically(-1)) { fab.extend() }

            }
        })

        arrangeSearchView()
        sendRequest()
    }

    /** This function is called whenever clicked add emergency button
     * It opens add Emergency fragment
     */
    private fun addEmergency(){
        val addEmergencyFragment = AddEmergencyFragment(emergencyViewModel,null)
        addFragment(addEmergencyFragment,"AddEmergencyFragment")
    }

    /** This function is called whenever emergency item is selected
     * It opens a emergency page that contains details about it
     */
    private fun openEmergencyItemFragment(emergency: EmergencyBody.EmergencyItem){
        val emergencyItemFragment = EmergencyItemFragment(emergencyViewModel,emergency)
        addFragment(emergencyItemFragment,"EmergencyItemFragment")
    }

    /** This function connects backend and get all emergency requests, then it observes livedata from viewModel which is changed
     * whenever all emergencies are fetched from backend. Then it creates a emergency list with this response and prepare recyclerView with this list
     */
    private fun sendRequest(queries: MutableMap<String, String>? = null) {
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        if (!emergencyViewModel.getAllEmergencies().isNullOrEmpty()){
            emergencyList = emergencyViewModel.getAllEmergencies()!!.map { EmergencyMapper.fromEmergencyToEmergencyItem(it) }
            arrangeRecyclerView(emergencyList)
        }

//        !!--- Emergency is currently running locally ---!!
//
//        if (requireActivity == null){ // to handle error when user enters this page twice
//            requireActivity = requireActivity()
//        }
//        emergencyViewModel.getLiveDataResponse().observe(requireActivity!!){ emergencyResponse ->
//            arrangeRecyclerView(emergencyResponse.emergencies)
//        }

    }

    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView(emergencyList : List<EmergencyBody.EmergencyItem>) {
        val recyclerView = binding.recyclerViewEmergencies
        if (recyclerView.layoutManager == null) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }

        val adapter = EmergencyAdapter(emergencyList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            openEmergencyItemFragment(it)
        }

    }


    /**
     * Arrange search view
     */
    private fun arrangeSearchView() {
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

        filterBinding.chUrgency.visibility = View.GONE
        filterBinding.cgSubTypes.visibility = View.GONE
        filterBinding.tvSubType.visibility = View.GONE

        // Set up Type Filter switch listener
        typeSwitch.setOnClickListener {
            typeLay.visibility = if (typeSwitch.isChecked) View.VISIBLE else View.GONE
        }



        // Set up Location Filter switch listener
        locationSwitch.setOnClickListener {
            locationLay.visibility = if (locationSwitch.isChecked) View.VISIBLE else View.GONE
        }

        // Set up map button click listener
        mapButton.setOnClickListener {
            if (mapButton.isChecked) {
                mapButton.text = getString(R.string.sf_location_select)
                etXCoordinate.text?.clear()
                etYCoordinate.text?.clear()
                mapButton.isChecked = false
            } else {
                mapFragment.isDialog = true // arrange that as a dialog instead of fragment
                mapFragment.coordinatesSelectedListener = this@EmergencyFragment
                mapFragment.show(parentFragmentManager, "mapDialog")
            }
        }

        // Set up apply button click listener
        applyButton.setOnClickListener {
            dialog.dismiss()
        }

        // Set up cancel button click listener
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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


    // Convert Emergency(local) to EmergencyItem(live) and vice-versa
    object EmergencyMapper {

        fun fromEmergencyItemToEmergency(emergencyItem: EmergencyBody.EmergencyItem): Emergency {
            return Emergency(
                ID = null,
                type = emergencyItem.type,
                description = emergencyItem.description,
                creatorName = emergencyItem.creator_name,
                contactNumber = emergencyItem.phone_number,
                location = emergencyItem.location,
                x = emergencyItem.x,
                y = emergencyItem.y
            )
        }

        fun fromEmergencyToEmergencyItem(emergency: Emergency): EmergencyBody.EmergencyItem {
            return EmergencyBody.EmergencyItem(
                type = emergency.type,
                description = emergency.description,
                creator_name = emergency.creatorName,
                phone_number = emergency.contactNumber,
                location = emergency.location,
                created_at = "-"
            )
        }
    }

}
