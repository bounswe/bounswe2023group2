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
import com.example.disasterresponseplatform.adapter.NeedAdapter
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.databinding.FragmentNeedBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager

class NeedFragment(private val needViewModel: NeedViewModel) : Fragment() {

    private lateinit var binding: FragmentNeedBinding
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
        binding.btAddNeed.setOnClickListener {
            addOrEditNeed(null)
        }

        arrangeSearchView()
        //arrangeRecyclerView()
        sendRequest()
    }

    /** This function is called whenever need is created or edited
     * If it is created need should be null, else need should be the clicked item
     */
    private fun addOrEditNeed(need: Need?){
        val token = DiskStorageManager.getKeyValue("token")
        if (!token.isNullOrEmpty()) {
            val addNeedFragment = AddNeedFragment(needViewModel,need)
            addFragment(addNeedFragment,"AddNeedFragment")
        }
        else{
            Toast.makeText(context, "You need to Logged In !", Toast.LENGTH_LONG).show()
        }
    }

    /** This function connects backend and get all need requests, then it observes livedata from viewModel which is changed
     * whenever all needs are fetched from backend. Then it creates a need list with this response and prepare recyclerView with this list
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
        // val list = needViewModel.getAllNeeds() // this is for local DB
        val adapter = NeedAdapter(needList)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            addOrEditNeed(it)
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

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}
