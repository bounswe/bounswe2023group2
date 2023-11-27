package com.example.disasterresponseplatform.ui.activity.resource

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ResourceAdapter
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.FragmentResourceBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

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

    /**
     * It refresh the recycler view whenever returned this page (i.e after adding/editing/deleting item)
     */
    override fun onResume() {
        super.onResume()
        sendRequest()
    }

    private fun arrangeView(){
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
                if (dy > 30 && fab.isExtended) {
                    fab.shrink()
                }

                // if the recycler view is scrolled
                // above extend the FAB
                if (dy < -30 && !fab.isExtended) {
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

    /** This function connects backend and get all resource requests, then it observes livedata from viewModel which is changed
     * whenever all resources are fetched from backend. Then it creates a resource list with this response and prepare recyclerView with this list
     */
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


    /** Arrange recycler view and its adapter
     * Whenever an item is clicked, it make toast and opens edit resource page
     */
    private fun arrangeRecyclerView(resourceList : List<Resource>){
        val recyclerView = binding.recyclerViewResources
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
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
        dialog.setContentView(R.layout.sort_and_filter)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
    }


    /** This function is called whenever resource item is selected
     * It opens a resource page that contains details about it and users can edit, delete, upvote and downvote this item from this page
     * if they have the authority
     */
    private fun openResourceItemFragment(resource: Resource){
        val resourceItemFragment = ResourceItemFragment(resourceViewModel,resource)
        addFragment(resourceItemFragment,"ResourceItemFragment")
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}
