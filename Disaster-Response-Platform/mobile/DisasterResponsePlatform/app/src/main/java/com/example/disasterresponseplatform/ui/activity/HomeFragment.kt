package com.example.disasterresponseplatform.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ViewPagerAdapter
import com.example.disasterresponseplatform.databinding.FragmentHomeBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.action.ActionFragment
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyFragment
import com.example.disasterresponseplatform.ui.activity.event.EventFragment
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceFragment
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
import com.example.disasterresponseplatform.utils.GeneralUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment(
    private val needViewModel: NeedViewModel,
    private val resourceViewModel: ResourceViewModel,
    private val actionViewModel: ActionViewModel,
    private val eventViewModel: EventViewModel,
    private val mainAct: MainActivity
) : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val fragments: ArrayList<Fragment> = arrayListOf(
            EmergencyFragment(),
            NeedFragment(needViewModel),
            ResourceFragment(resourceViewModel),
            EventFragment(eventViewModel),
            ActionFragment(actionViewModel)
        )

        val tabTitles: ArrayList<String> = arrayListOf(
            getString(R.string.tab_emergency),
            getString(R.string.tab_need),
            getString(R.string.tab_resource),
            getString(R.string.tab_event),
            getString(R.string.tab_action)
        )

        val adapter = ViewPagerAdapter(fragments, mainAct)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = fragments.size // to prevent selected 4th tab gives error issue

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            // Change color when tab is selected
            tab.view.setOnClickListener {
                changeTabColor(position)
                changeActionBarColor(position)
                changeStatusBarColor(position)
                viewPager.setCurrentItem(position, false) // Update the selected tab
            }
        }.attach()

        // Allows colors to change when changing tabs by sliding
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    changeTabColor(position)
                    changeActionBarColor(position)
                    changeStatusBarColor(position)
                    viewPager.setCurrentItem(position, true) // Update the selected tab
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return binding.root

    }

    // Change action bar color based on tab position
    public fun changeActionBarColor(position: Int) {
        val actionBar = mainAct.supportActionBar
        actionBar?.let {
            val color = when (position) {
                0 -> R.color.colorEmergency
                1 -> R.color.colorNeed
                2 -> R.color.colorResource
                3 -> R.color.colorEvent
                4 -> R.color.colorAction
                else -> R.color.primary
            }
            it.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mainAct, color)))
        }
    }

    // Change status bar color based on tab position
    fun changeStatusBarColor(position: Int) {
        val window: Window = requireActivity().window
        val color = when (position) {
            0 -> R.color.colorEmergency
            1 -> R.color.colorNeed
            2 -> R.color.colorResource
            3 -> R.color.colorEvent
            4 -> R.color.colorAction
            else -> R.color.primary
        }
        window.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

    // Change tab color based on tab position
    private fun changeTabColor(position: Int) {
        val tab = binding.tabLayout
        val color = when (position) {
            0 -> R.color.colorEmergency
            1 -> R.color.colorNeed
            2 -> R.color.colorResource
            3 -> R.color.colorEvent
            4 -> R.color.colorAction
            else -> R.color.primary
        }
        tab.background = ColorDrawable(ContextCompat.getColor(mainAct, color))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeActionBarColor(0)
        changeStatusBarColor(0)
        changeTabColor(0)
        checkInternetConnection()
    }

    /**
     * This function is called when the home page is shown at first or navigating that page
     * from other pages (map, profile)
     * It will check the internet connection
     * If it is connected, it checks whether it has some objects in local database that is created without a connection
     * If it is created in local, then it will post these objects to the backend and delete them from local database.
     * else nothing has changed
     * If it posts some objects into backend it shows a toast
     * If it has no connection, just logs no connection and continue to its life
     */
    private fun checkInternetConnection(){
        if (requireActivity == null) requireActivity = requireActivity()
        if (GeneralUtil.isInternetAvailable(requireContext())){
            Log.i("InternetConnection","OK")
            GeneralUtil.checkLocalChanges(needViewModel,resourceViewModel,eventViewModel,requireActivity!!)
            val timer = GeneralUtil.getIsPostedTimer()
            timer.start() // start timer
            GeneralUtil.getIsPosted().observe(requireActivity!!){isPosted ->
                if (isPosted){
                    timer.cancel() // cancel it to not return false
                    if (isAdded) // check whether it is attached a require context
                        Toast.makeText(requireContext(),"Your Local Objects are Posted into Backend Successfully",Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Log.i("InternetConnection","NOT OK")
        }
    }



}

