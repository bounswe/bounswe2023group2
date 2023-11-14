package com.example.disasterresponseplatform.ui.activity.need

import android.os.Bundle
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
import com.example.disasterresponseplatform.adapter.ActivityAdapter
import com.example.disasterresponseplatform.adapter.NeedAdapter
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.FragmentNeedBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.resource.AddResourceFragment

class NeedFragment(private val needViewModel: NeedViewModel) : Fragment() {

    private lateinit var binding: FragmentNeedBinding
    private lateinit var addNeedFragment: AddNeedFragment
    private lateinit var searchView: SearchView
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNeedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()
    }

    private fun arrangeView(){
        // add need clickable
        binding.btAddNeed.setOnClickListener {
            addOrEditNeed(null)
        }
        arrangeSearchView()
        sendRequest()
    }

    /** This function connects backend and get all resource requests, then it observes livedata from viewModel which is changed
     * whenever all resources are fetched from backend. Then it creates a need list with this response and prepare recyclerView with this list
     */
    private fun sendRequest(){
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        needViewModel.sendGetAllRequest()
        needViewModel.getLiveDataResponse().observe(requireActivity!!){ needResponse ->
            val lst = needViewModel.createNeedList(needResponse)
            arrangeRecyclerView(lst)
        }
    }

    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView(needList : List<Need>){
        val recyclerView = binding.recyclerViewNeeds
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }

        val list = needViewModel.getAllNeeds() // from local db
        val adapter = NeedAdapter(needList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            // Handle item click by navigating to EditNeedFragment
            addOrEditNeed(it)
        }
    }

    private fun addOrEditNeed(need: Need?){
        val token = DiskStorageManager.getKeyValue("token")
        if (!token.isNullOrEmpty()) {
            if (need == null)
                addFragment(AddNeedFragment(needViewModel))
            else
                addFragment(EditNeedFragment(needViewModel,need))
        }
        else{
            Toast.makeText(context, "You need to Logged In !", Toast.LENGTH_LONG).show()
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
