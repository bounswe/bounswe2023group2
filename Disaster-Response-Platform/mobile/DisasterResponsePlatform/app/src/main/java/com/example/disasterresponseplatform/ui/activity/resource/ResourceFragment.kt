package com.example.disasterresponseplatform.ui.activity.resource

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
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
import com.example.disasterresponseplatform.adapter.ResourceAdapter
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.databinding.FragmentResourceBinding
import com.example.disasterresponseplatform.databinding.SortAndFilterBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResourceFragment(private val resourceViewModel: ResourceViewModel) : Fragment() {

    private lateinit var binding: FragmentResourceBinding
    private lateinit var filterBinding: SortAndFilterBinding
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourceBinding.inflate(inflater,container,false)
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

        binding.btAddResource.setOnClickListener {
            addResource()
        }

        val fab: ExtendedFloatingActionButton = binding.btAddResource

        binding.recyclerViewResources.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // if the recycler view is scrolled
                // above shrink the FAB
                if (dy > 10 && fab.isExtended) {
                    fab.shrink()
                }

                // if the recycler view is scrolled
                // above extend the FAB
                if (dy < -10 && !fab.isExtended) {
                    fab.extend()
                }

                // of the recycler view is at the first
                // item always extend the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    fab.extend()
                }
            }
        })


        arrangeSearchView()
        //arrangeRecyclerView()
        sendRequest()
    }

    /** This function is called whenever resource is created or edited
     * If it is created resource should be null, else resource should be the clicked item
     */
    private fun addResource(){
        val token = DiskStorageManager.getKeyValue("token")
        if (!token.isNullOrEmpty()) {
            val addResourceFragment = AddResourceFragment(resourceViewModel,null)
            addFragment(addResourceFragment,"AddResourceFragment")
        }
        else{
            Toast.makeText(context, "You need to Logged In !", Toast.LENGTH_LONG).show()
        }
    }

    /** This function is called whenever resource item is selected
     * It opens a resource page that contains details about it and users can edit, delete, upvote and downvote this item from this page
     * if they have the authority
     */
    private fun openResourceItemFragment(resource: ResourceBody.ResourceItem){
        val resourceItemFragment = ResourceItemFragment(resourceViewModel,resource)
        addFragment(resourceItemFragment,"ResourceItemFragment")
    }

    /** This function connects backend and get all resource requests, then it observes livedata from viewModel which is changed
     * whenever all resources are fetched from backend. Then it creates a resource list with this response and prepare recyclerView with this list
     */
    private fun sendRequest(queries: MutableMap<String, String>? = null) {

        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        resourceViewModel.sendGetAllRequest(queries)
        resourceViewModel.getLiveDataResponse().observe(requireActivity!!){ resourceResponse ->
            arrangeRecyclerView(resourceResponse.resources)
        }
    }


    /** Arrange recycler view and its adapter
     * Whenever an item is clicked, it make toast and opens edit resource page
     */
    private fun arrangeRecyclerView(resourceList : List<ResourceBody.ResourceItem>){
        val recyclerView = binding.recyclerViewResources
        if (recyclerView.layoutManager == null && isAdded){
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        // val list = resourceViewModel.getAllResources() // this is for local DB
        val adapter = ResourceAdapter(resourceList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            openResourceItemFragment(it)
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
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val applyButton = filterBinding.btApply
        val cancelButton = filterBinding.btCancel
        val typesChipGroup = filterBinding.cgTypes
        val subtypesChipGroup = filterBinding.cgSubTypes
        val subtypesTitle = filterBinding.tvSubType

        filterBinding.chUrgency.visibility = GONE

        // Add chips from resource_types array into Type chip group
        for (resourceType in resources.getStringArray(R.array.resource_types)) {
            val chip = Chip(requireContext())
            chip.text = resourceType
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
                typeChanged(checkedChip.text.toString())
            }
        }

        // Set up apply button click listener
        applyButton.setOnClickListener {
            sendRequest(gatherFilterDetails())
            // Trigger the function with the selected parameters
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
        val selectedSortBy = sortByChipGroup.findViewById<Chip>(selectedSortById)?.text.toString()

        val orderChipGroup = filterBinding.cgSortOrder
        val selectedOrderId = filterBinding.cgSortOrder.checkedChipId
        val selectedOrder = orderChipGroup.findViewById<Chip>(selectedOrderId)?.text.toString()

        val typesChipGroup = filterBinding.cgTypes
        val selectedTypeIds = filterBinding.cgTypes.checkedChipIds
        val selectedTypes = mutableListOf<String>()

        val subtypesChipGroup = filterBinding.cgSubTypes
        val selectedSubTypeIds = subtypesChipGroup.checkedChipIds
        val selectedSubTypes = mutableListOf<String>()

        for (chipId in selectedTypeIds) {
            val chip = typesChipGroup.findViewById<Chip>(chipId)
            chip?.let { selectedTypes.add(it.text.toString()) }
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
            if (selectedTypes.isNotEmpty()) {
                queries["types"] = selectedTypes.joinToString(",")
            }
        }
        selectedSubTypes.let { selectedSubTypes ->
            if (selectedSubTypes.isNotEmpty()) {
                queries["subtypes"] = selectedSubTypes.joinToString(",")
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
                                val formFieldsResourceResponse = gson.fromJson(
                                    rawJson,
                                    ResourceBody.ResourceFormFieldsResponse::class.java
                                )
                                if (formFieldsResourceResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsResourceResponse: $formFieldsResourceResponse"
                                    )

                                    val fields: List<ResourceBody.ResourceFormFields> =
                                        formFieldsResourceResponse.fields

                                    // get subtype field from the response and add them into subtypesChipGroup
                                    for (field in fields) {
                                        if (field.type == "select") {
                                            if (field.name == "subtype") {

                                                val options = field.options ?: emptyList()
                                                for (resourceType in options) {
                                                    val chip = Chip(requireContext())
                                                    chip.text = resourceType
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

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}
