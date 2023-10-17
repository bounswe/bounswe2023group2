package com.example.disasterresponseplatform.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentHomePageBinding
import com.example.disasterresponseplatform.ui.activity.ActivityFragment
import com.example.disasterresponseplatform.ui.map.MapFragment
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import com.example.disasterresponseplatform.ui.registration.LoginFragment


class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private val loginFragment = LoginFragment()
    private val mapFragment = MapFragment()
    private val activityFragment = ActivityFragment()
    private val profileFragment = ProfileFragment()

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
        clickButtons()
    }

    /**
     * This defines what buttons will do when they are clicked, you can do that easily by observing it
     * with setOnClickListener method.
     */
    private fun clickButtons(){
        binding.btLogin.setOnClickListener {
            addFragment(loginFragment)
        }
        binding.btMap.setOnClickListener {
            addFragment(mapFragment)
        }
        binding.btActivity.setOnClickListener {
            addFragment(activityFragment)
        }
        binding.btProfile.setOnClickListener {
            addFragment(profileFragment)
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