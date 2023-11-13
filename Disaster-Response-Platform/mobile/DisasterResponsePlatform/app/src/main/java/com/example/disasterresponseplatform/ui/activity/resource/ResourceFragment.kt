package com.example.disasterresponseplatform.ui.activity.resource

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ResourceAdapter
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.FragmentResourceBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager

class ResourceFragment(private val resourceViewModel: ResourceViewModel) : Fragment() {

    private lateinit var binding: FragmentResourceBinding
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()
    }

    private fun arrangeView(){
        binding.btAddResource.setOnClickListener {
            val token = DiskStorageManager.getKeyValue("token")
            if (!token.isNullOrEmpty()) {
                val addResourceFragment = AddResourceFragment(resourceViewModel)
                addFragment(addResourceFragment)
            }
            else{
                Toast.makeText(context, "You need to Logged In !", Toast.LENGTH_LONG).show()
            }
        }

        arrangeSearchView()
        //arrangeRecyclerView()
        sendRequest()
    }

    private fun sendRequest(){
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        resourceViewModel.sendGetAllRequest()
        resourceViewModel.getLiveDataResponse().observe(requireActivity!!){ resourceResponse ->
            val lst = resourceViewModel.createResourceList(resourceResponse)
            arrangeRecyclerView(lst)
        }
    }


    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView(resourceList : List<Resource>){
        val recyclerView = binding.recyclerViewNeeds
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }
        val list = resourceViewModel.getAllResources()
        val adapter = ResourceAdapter(resourceList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            val text = "Type: ${it?.type}, Details: ${it?.details}, x: ${it?.coordinateX}, y: ${it?.coordinateY},\" "+
                    "Date: ${it?.creationTime}, Quantity: ${it?.quantity}, Condition: ${it?.condition}"
            Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
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

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
