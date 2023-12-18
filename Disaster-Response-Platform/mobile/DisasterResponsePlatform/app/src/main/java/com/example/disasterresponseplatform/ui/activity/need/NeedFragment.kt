package com.example.disasterresponseplatform.ui.activity.need

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.NeedAdapter
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.databinding.FragmentNeedBinding
import com.example.disasterresponseplatform.databinding.SortAndFilterBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NeedFragment(
    private val needViewModel: NeedViewModel
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentNeedBinding
    private lateinit var filterBinding: SortAndFilterBinding
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null
    private val mapFragment = ActivityMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNeedBinding.inflate(inflater,container,false)
        if (isAdded) sendRequest() // to prevent not attached error, check whether it attached a context first
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

        binding.btAddNeed.setOnClickListener {
            addNeed()
        }

        val fab: ExtendedFloatingActionButton = binding.btAddNeed

        binding.recyclerViewNeeds.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    /** This function is called whenever clicked add need button
     * It opens add Need fragment if user is authenticated, else warns the user
     */
    private fun addNeed(){
        val token = DiskStorageManager.getKeyValue("token")
        if (DiskStorageManager.hasKey("token") && !token.isNullOrEmpty()) {
            val addNeedFragment = AddNeedFragment(needViewModel,null)
            addFragment(addNeedFragment,"AddNeedFragment")
        }
        else{
            Toast.makeText(context, getString(R.string.pr_login_required), Toast.LENGTH_LONG).show()
        }
    }

    /** This function is called whenever need item is selected
     * It opens a need page that contains details about it and users can edit, delete, upvote and downvote this item from this page
     * if they have the authority
     */
    private fun openNeedItemFragment(need: NeedBody.NeedItem){
        val needItemFragment = NeedItemFragment(needViewModel,need)
        addFragment(needItemFragment,"NeedItemFragment")
    }

    /** This function connects backend and get all need requests, then it observes livedata from viewModel which is changed
     * whenever all needs are fetched from backend. Then it creates a need list with this response and prepare recyclerView with this list
     */
    private fun sendRequest(queries: MutableMap<String, String>? = null) {

        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        needViewModel.sendGetAllRequest(queries)
        needViewModel.getLiveDataResponse().observe(requireActivity!!){ needResponse ->
            arrangeRecyclerView(needResponse.needs)
        }
    }

    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView(needList : List<NeedBody.NeedItem>){
        val recyclerView = binding.recyclerViewNeeds
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }
        // val list = needViewModel.getAllNeeds() // this is for local DB
        val adapter = NeedAdapter(needList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            openNeedItemFragment(it)
        }
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


    /**
     * Arrange filter and sort
     */
    private fun showFilterDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filterBinding = SortAndFilterBinding.inflate(layoutInflater)
        dialog.setContentView(filterBinding.root)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
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
        val subtypesChipGroup = filterBinding.cgSubTypes
        val subtypesTitle = filterBinding.tvSubType

        // Add chips from need_types array into Type chip group
        for (needType in resources.getStringArray(R.array.need_types)) {
            val chip = Chip(requireContext())
            chip.text = needType
            chip.isCheckable = true
            chip.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            typesChipGroup.addView(chip)
        }

        // Set up type chips select listener
        typesChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkedChip = group.findViewById<Chip>(checkedId)
            // Perform actions with the checked chip here
            if(checkedChip == null) {
                // Delete all type-specific fields
                filterBinding.cgSubTypes.removeAllViews()
                subtypesTitle.visibility = ViewGroup.GONE
            } else {
                subtypesTitle.visibility = ViewGroup.VISIBLE
                typeChanged(convertTypeLabelToName(checkedChip.text.toString()))
            }
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
                mapFragment.coordinatesSelectedListener = this@NeedFragment
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
            getString(R.string.sf_urgency) -> "urgency"
            else -> ""
        }

        val selectedOrder = "desc"

        val typeSwitch = filterBinding.swTypeFilter
        val locationSwitch = filterBinding.swLocationFilter

        val typesChipGroup = filterBinding.cgTypes
        val selectedTypeIds = filterBinding.cgTypes.checkedChipIds
        val selectedTypes = mutableListOf<String>()

        val subtypesChipGroup = filterBinding.cgSubTypes
        val selectedSubTypeIds = subtypesChipGroup.checkedChipIds
        val selectedSubTypes = mutableListOf<String>()

        val selectedXCoordinate = filterBinding.etCoordinateX.text.toString()
        val selectedYCoordinate = filterBinding.etCoordinateY.text.toString()
        val selectedMaxDistance = filterBinding.slDistance.value.toString()

        for (chipId in selectedTypeIds) {
            val chip = typesChipGroup.findViewById<Chip>(chipId)
            chip?.let {
                selectedTypes.add(convertTypeLabelToName(it.text.toString()))
            }
        }

        for (chipId in selectedSubTypeIds) {
            val chip = subtypesChipGroup.findViewById<Chip>(chipId)
            chip?.let { selectedSubTypes.add(it.text.toString()) }
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
        selectedSubTypes.let { selectedSubTypes ->
            if (typeSwitch.isChecked && selectedSubTypes.isNotEmpty()) {
                queries["subtypes"] = selectedSubTypes.joinToString(",")
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


    /**
     * This function triggered when Type field changed.
     * Makes api call to retrieve type-specific form fields of selected type.
     * Creates type-specific form fields.
     * Adds these fields into laySpecific layout that holds type-specific fields such as allergens, expiration_date.
     * Creates subtype adapter and connects it.
     */
    private fun typeChanged(selectedType: String) {

        // Delete all type-specific fields
        val subtypesChipGroup = filterBinding.cgSubTypes
        subtypesChipGroup.removeAllViews()

        // Find type-specific fields and subType list
        val networkManager = NetworkManager()
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )

        networkManager.makeRequest(
            endpoint = Endpoint.FORM_FIELDS_TYPE,
            requestType = RequestType.GET,
            id = selectedType,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val formFieldsNeedResponse = gson.fromJson(
                                    rawJson,
                                    NeedBody.NeedFormFieldsResponse::class.java
                                )
                                if (formFieldsNeedResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsNeedResponse: $formFieldsNeedResponse"
                                    )

                                    val fields: List<NeedBody.NeedFormFields> =
                                        formFieldsNeedResponse.fields

                                    // get subtype field from the response and add them into subtypesChipGroup
                                    for (field in fields) {
                                        if (field.type == "select") {
                                                if (field.name == "subtype") {

                                                    val options = field.options ?: emptyList()
                                                    for (needType in options) {
                                                        val chip = Chip(requireContext())
                                                        chip.text = needType
                                                        chip.isCheckable = true
                                                        chip.layoutParams = LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        )
                                                        subtypesChipGroup.addView(chip)
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseSuccess", "Body: $errorBody")
                        }
                    }
                }
            }
        )
    }

    private fun convertTypeLabelToName(typeLabel: String) : String {
        val typeName = when (typeLabel) {
            getString(R.string.cloth) -> "cloth"
            getString(R.string.food) -> "food"
            getString(R.string.drink) -> "drink"
            getString(R.string.shelter) -> "shelter"
            getString(R.string.medication) -> "medication"
            getString(R.string.transportation) -> "transportation"
            getString(R.string.tool) -> "tool"
            getString(R.string.human) -> "human"
            getString(R.string.other) -> "other"
            else -> ""
        }
        return typeName
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
