package com.example.disasterresponseplatform.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentProfileBinding
import com.example.disasterresponseplatform.ui.profile.notification.SubscribeNotificationFragment
import com.example.disasterresponseplatform.ui.profile.pastUserActions.PastUserActionsFragment


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val pastUserActionsFragment = PastUserActionsFragment()
    private val subscribeNotificationFragment = SubscribeNotificationFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButtons()
    }

    private fun clickButtons(){
        binding.btUserActivities.setOnClickListener {
            addFragment(pastUserActionsFragment)
        }
        binding.btSubscribedNotification.setOnClickListener {
            addFragment(subscribeNotificationFragment)
        }
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

}