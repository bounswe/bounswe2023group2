package com.example.disasterresponseplatform.ui.activity.need

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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
        sendRequest()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()
        val btFilter = binding.btFilter
        btFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun arrangeView(){
        binding.btAddNeed.setOnClickListener {
            addNeed()
        }

        arrangeSearchView()
        //arrangeRecyclerView()
        sendRequest()
    }

    /** This function is called whenever clicked add need button
     * It opens add Need fragment if user is authenticated, else warns the user
     */
    private fun addNeed(){
        val token = DiskStorageManager.getKeyValue("token")
        if (!token.isNullOrEmpty()) {
            val addNeedFragment = AddNeedFragment(needViewModel,null)
            addFragment(addNeedFragment,"AddNeedFragment")
        }
        else{
            Toast.makeText(context, "You need to Logged In !", Toast.LENGTH_LONG).show()
        }
    }

    /** This function is called whenever need item is selected
     * It opens a need page that contains details about it and users can edit, delete, upvote and downvote this item from this page
     * if they have the authority
     */
    private fun openNeedItemFragment(need: Need){
        val needItemFragment = NeedItemFragment(needViewModel,need)
        addFragment(needItemFragment,"NeedItemFragment")
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
        dialog.setContentView(R.layout.sort_and_filter)
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }
}
