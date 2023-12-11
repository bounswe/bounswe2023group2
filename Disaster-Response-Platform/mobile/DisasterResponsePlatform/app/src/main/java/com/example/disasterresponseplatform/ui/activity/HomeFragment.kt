package com.example.disasterresponseplatform.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.ViewPagerAdapter
import com.example.disasterresponseplatform.databinding.FragmentHomeBinding
import com.example.disasterresponseplatform.ui.activity.action.ActionFragment
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyFragment
import com.example.disasterresponseplatform.ui.activity.event.EventFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceFragment
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment(
    private val needViewModel: NeedViewModel,
    private val resourceViewModel: ResourceViewModel,
    private val actionViewModel: ActionViewModel,
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
            EventFragment(),
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

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

