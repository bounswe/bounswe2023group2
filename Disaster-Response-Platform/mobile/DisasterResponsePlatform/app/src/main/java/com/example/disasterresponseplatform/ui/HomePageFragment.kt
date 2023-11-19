package com.example.disasterresponseplatform.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ActivityAdapter
import com.example.disasterresponseplatform.data.enums.ActivityEnum
import com.example.disasterresponseplatform.data.models.DummyActivity
import com.example.disasterresponseplatform.data.enums.PredefinedTypes
import com.example.disasterresponseplatform.databinding.FragmentHomePageBinding
import com.example.disasterresponseplatform.utils.DateUtil


class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var searchView: SearchView

    /**
     * This defines the layout
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    /**
     * This defines what this fragment will be do when your view is Created ( n the screen)
     * like onCreate method of activities
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeSearchView()
        arrangeRecyclerView()
    }

    private fun arrangeRecyclerView(){
        val recyclerView = binding.recyclerViewActivities
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val list = prepareDummyList()
        val adapter = ActivityAdapter(list)
        binding.adapter = adapter

        // this observes getLiveIntent, whenever a value is posted it enters this function
        adapter.getLiveIntent().observe(requireActivity()){
            val text = "Action: ${it.activityType}, Type: ${it.predefinedTypes}, Location: ${it.location}, " +
                    "Date: ${it.date}, Reliability Scale: ${it.reliabilityScale},"
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
    }

    private fun prepareDummyList(): MutableList<DummyActivity>{
        val list = mutableListOf<DummyActivity>()
        list.add(
            DummyActivity(ActivityEnum.Need,
            PredefinedTypes.Food,"Gaziantep","${DateUtil.getDate("yyyy-MM-dd")} ${DateUtil.getTime("HH:mm:ss")}",0.29)
        )
        list.add(
            DummyActivity(ActivityEnum.Resource,
            PredefinedTypes.Food,"İstanbul","${DateUtil.getDate("yyyy-MM-dd")} ${DateUtil.getTime("HH:mm:ss")}",0.92)
        )
        list.add(
            DummyActivity(ActivityEnum.Resource,
            PredefinedTypes.Human,"Bursa","22.10.2023",0.88)
        )
        list.add(DummyActivity(ActivityEnum.Need, PredefinedTypes.Clothes,"Hatay","22.10.2023",0.53))
        list.add(
            DummyActivity(ActivityEnum.Event,
            PredefinedTypes.Collapse,"Kahramanmaraş","20.10.2023",0.76)
        )
        list.add(
            DummyActivity(ActivityEnum.Emergency,
            PredefinedTypes.Debris,"Kahramanmaraş","21.10.2023",0.15)
        )
        return list
    }

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
     * This method adds a fragment to parentFragmentManager which is our FragmentManager on MainActivity.
     * It can do that by adding a backStack
     */
    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    /**
     * This method can be used for back buttons. It pop one fragment from backStack
     */
    fun popFragment() {
        parentFragmentManager.popBackStack()
    }

    /**
     * This method only replace a fragment, because it doesn't add anything on backStack
     */
    fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) //replacing fragment
            commit() //call signals to the FragmentManager that all operations have been added to the transaction
        }
    }

}