package com.example.disasterresponseplatform.ui.activity.emergency

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.adapter.EmergencyAdapter
import com.example.disasterresponseplatform.databinding.FragmentEmergencyBinding
import com.example.disasterresponseplatform.databinding.SortAndFilterBinding
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class EmergencyFragment : Fragment(), OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentEmergencyBinding
    private lateinit var filterBinding: SortAndFilterBinding
    private val addEmergencyFragment = AddEmergencyFragment()
    private val mapFragment = ActivityMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmergencyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrangeView()
    }

    /**
     * Arrange recycler view and its adapter
     */
    private fun arrangeRecyclerView() {
        val recyclerView = binding.recyclerViewEmergency
        if (recyclerView.layoutManager == null) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
        }
        // val list = needViewModel.getAllNeeds() // this is for local DB
        val emergencyList = mutableListOf<String>()
        emergencyList.add("Emergency 1")
        val adapter = EmergencyAdapter(emergencyList)
        binding.adapter = adapter
    }


    private fun arrangeView(){
        arrangeRecyclerView()
        binding.btFilter.setOnClickListener {
            showFilterDialog()
        }

        binding.btAddEmergency.setOnClickListener {
            addFragment(addEmergencyFragment)
        }

        val fab: ExtendedFloatingActionButton = binding.btAddEmergency

        binding.recyclerViewEmergency.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Extend and shrink the Floating Action Button
                if (dy > 10 && fab.isExtended) { fab.shrink() }
                if (dy < -10 && !fab.isExtended) { fab.extend() }
                if (!recyclerView.canScrollVertically(-1)) { fab.extend() }

            }
        })

        //arrangeSearchView()
        //arrangeRecyclerView()
        //sendRequest()
    }


    /**
     * Arrange filter and sort
     */
    private fun showFilterDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        filterBinding = SortAndFilterBinding.inflate(layoutInflater)
        dialog.setContentView(filterBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val locationSwitch = filterBinding.swLocationFilter
        val locationLay = filterBinding.layLocationFilter
        val etXCoordinate = filterBinding.etCoordinateX
        val etYCoordinate = filterBinding.etCoordinateY
        val typeSwitch = filterBinding.swTypeFilter
        val typeLay = filterBinding.layTypeFilter
        val applyButton = filterBinding.btApply
        val cancelButton = filterBinding.btCancel
        val mapButton = filterBinding.btSelectFromMap

        filterBinding.chUrgency.visibility = View.GONE
        filterBinding.cgSubTypes.visibility = View.GONE
        filterBinding.tvSubType.visibility = View.GONE

        // Set up Type Filter switch listener
        typeSwitch.setOnClickListener {
            typeLay.visibility = if (typeSwitch.isChecked) View.VISIBLE else View.GONE
        }



        // Set up Location Filter switch listener
        locationSwitch.setOnClickListener {
            locationLay.visibility = if (locationSwitch.isChecked) View.VISIBLE else View.GONE
        }

        // Set up map button click listener
        mapButton.setOnClickListener {
            if (mapButton.isChecked) {
                mapButton.text = getString(R.string.sf_location_select)
                etXCoordinate.text?.clear()
                etYCoordinate.text?.clear()
                mapButton.isChecked = false
            } else {
                mapFragment.isDialog = true // arrange that as a dialog instead of fragment
                mapFragment.coordinatesSelectedListener = this@EmergencyFragment
                mapFragment.show(parentFragmentManager, "mapDialog")
            }
        }

        // Set up apply button click listener
        applyButton.setOnClickListener {
            dialog.dismiss()
        }

        // Set up cancel button click listener
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onCoordinatesSelected(x: Double, y: Double) {
        filterBinding.etCoordinateX.setText(x.toString())
        filterBinding.etCoordinateY.setText(y.toString())
        filterBinding.btSelectFromMap.isChecked = true
        filterBinding.btSelectFromMap.text = getString(R.string.sf_location_selected)
    }
}
