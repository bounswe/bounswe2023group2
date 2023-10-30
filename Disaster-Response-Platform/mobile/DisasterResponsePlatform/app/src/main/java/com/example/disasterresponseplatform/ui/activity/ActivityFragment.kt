package com.example.disasterresponseplatform.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentActivityBinding
import com.example.disasterresponseplatform.ui.activity.action.ActionFragment
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyFragment
import com.example.disasterresponseplatform.ui.activity.event.EventFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceFragment

class ActivityFragment(private val needViewModel: NeedViewModel) : Fragment() {

    private lateinit var binding: FragmentActivityBinding
    private val emergencyFragment = EmergencyFragment()
    private val actionFragment = ActionFragment()
    private val eventFragment = EventFragment()
    private lateinit var needFragment:NeedFragment
    private val resourceFragment = ResourceFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btEmergency.setOnClickListener {
            addFragment(emergencyFragment)
        }
        binding.btAction.setOnClickListener {
            addFragment(actionFragment)
        }
        binding.btEvent.setOnClickListener {
            addFragment(eventFragment)
        }
        binding.btNeed.setOnClickListener {
            needFragment = NeedFragment(needViewModel)
            addFragment(needFragment)
        }
        binding.btResource.setOnClickListener {
            addFragment(resourceFragment)
        }
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
