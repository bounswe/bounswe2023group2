package com.example.disasterresponseplatform.ui.activity.resource

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentResourceBinding

class ResourceFragment : Fragment() {

    private lateinit var binding: FragmentResourceBinding
    private val addResourceFragment = AddResourceFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResourceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btAddResource.setOnClickListener {
            addFragment(addResourceFragment)
        }
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}
