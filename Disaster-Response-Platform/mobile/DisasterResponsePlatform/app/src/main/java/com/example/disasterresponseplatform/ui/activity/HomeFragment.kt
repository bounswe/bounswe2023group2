package com.example.disasterresponseplatform.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ViewPagerAdapter
import com.example.disasterresponseplatform.databinding.FragmentHomeBinding
import com.example.disasterresponseplatform.ui.activity.action.ActionFragment
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyFragment
import com.example.disasterresponseplatform.ui.activity.event.EventFragment
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceFragment
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
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
    public fun changeStatusBarColor(position: Int) {
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

