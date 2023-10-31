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
import com.example.disasterresponseplatform.databinding.FragmentNeedBinding

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
            addNeedFragment = AddNeedFragment(needViewModel)
            addFragment(addNeedFragment)
        }
        arrangeSearchView()
        arrangeRecyclerView()
    }

    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView(){

        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        val recyclerView = binding.recyclerViewNeeds
        if (recyclerView.layoutManager == null){
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }

        val list = needViewModel.getAllNeeds()
        val adapter = NeedAdapter(list)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity!!){
            val text = "Type: ${it?.type}, Details: ${it?.details}, Location: ${it?.location}, " +
                    "Date: ${it?.creationTime}, Quantity: ${it?.quantity}, Urgency: ${it?.urgency}"
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
