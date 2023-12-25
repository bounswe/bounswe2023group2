package com.example.disasterresponseplatform.ui.activity.emergency

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.databinding.FragmentAddEmergencyBinding
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.properties.Delegates

class AddEmergencyFragment(
    private val emergencyViewModel: EmergencyViewModel,
    private val emergency: EmergencyBody.EmergencyItem?
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddEmergencyBinding
    private var requireActivity: FragmentActivity? = null
    private var selectedType: String = ""
    private var selectedLocationX by Delegates.notNull<Double>()
    private var selectedLocationY by Delegates.notNull<Double>()
    private val mapFragment = ActivityMap()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEmergencyBinding.inflate(inflater, container, false)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        trackUserPickLocation()
        binding.etCoordinate.setStartIconOnClickListener {
            navigateToMapFragment()
        }
        setupButtons()
        submitEmergency(emergency == null)
        return binding.root
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.btnDebris,
            binding.btnFire,
            binding.btnHealth
        )

        binding.btnDebris.isChecked = true
        selectedType = "debris"

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach { it.isChecked = false }
                button.isChecked = true

                selectedType = when (button) {
                    binding.btnDebris -> "debris"
                    binding.btnFire -> "fire"
                    binding.btnHealth -> "medical"
                    else -> ""
                }
            }
        }
    }

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitEmergency(isAdd: Boolean) {
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            val layAddEmergency = binding.layAddEmergency
            if (validateFields(layAddEmergency)) {

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                val newEmergency = Emergency(
                    ID = null,
                    type = selectedType,
                    description = binding.etDescription.text.toString(),
                    creatorName = binding.etFullname.text.toString(),
                    contactNumber = binding.etPhoneNumber.text.toString(),
                    location = binding.etCoordinate.editText?.text.toString(),
                    x = selectedLocationX,
                    y = selectedLocationY)
                emergencyViewModel.insertEmergency(newEmergency) //insert local db

                if (isAdded) { // to ensure it attached a context
                    Toast.makeText(requireContext(), "It is saved in Local, It will be uploaded when you connect to the internet", Toast.LENGTH_LONG).show()
                }
                parentFragmentManager.popBackStack()

//              !!--- Emergency is currently running locally ---!!
//
//                emergencyViewModel.getLiveDataEmergencyID().observe(requireActivity!!) {
//                    if (it != "-1") { // in error cases it returns this
//                        if (isAdded) { // to ensure it attached a context
//                            if (isAdd)
//                                Toast.makeText(requireContext(), "Created Emergency ID: $it", Toast.LENGTH_LONG).show()
//                            else
//                                Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT).show()
//                        }
//
//                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
//                            if (isAdded) // to ensure it attached a parentFragmentManager
//                                parentFragmentManager.popBackStack("AddEmergencyFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            if (!isAdd)
//                                parentFragmentManager.popBackStack("EmergencyItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                            // Re-enable the button after the background operation completes
//                            binding.btnSubmit.isEnabled = true
//                        }, 200)
//                    } else {
//                        if (isAdded)
//                            Toast.makeText(requireContext(), "Error Check Logs", Toast.LENGTH_SHORT).show()
//                        binding.btnSubmit.isEnabled = true
//                    }
//                }

            } else {
                if (isAdded)
                    Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
                binding.btnSubmit.isEnabled = true
            }
        }
    }


    private fun validateFields(layout: ViewGroup): Boolean {
        var isValid = true

        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)

            if (view is TextInputLayout) {
                val editText = view.editText
                if (editText != null) {
                    val hint = editText.hint
                    if (hint != "Occur At" && hint != "Recurrence Rate" && hint != "Recurrence Deadline" && hint != "Description") {
                        val text = editText.text.toString().trim()
                        if (text.isEmpty()) {
                            isValid = false
                            view.error = "Cannot be empty"
                        } else {
                            view.error = null
                            view.isErrorEnabled = false
                        }
                    }
                }
            }

            // If there are nested layouts, recursively check their children
            if (view is ViewGroup) {
                isValid = isValid && validateFields(view)
            }
        }
        return isValid
    }

    private fun trackUserPickLocation(){
        mapFragment.getLocationChosen().observe(requireActivity!!){chosen ->
            if (chosen){
                Log.i("LocationMAP","IS CHOSEN")
                parentFragmentManager.setFragmentResultListener(
                    "coordinatesKey",
                    viewLifecycleOwner
                ) { _, bundle ->
                    selectedLocationX = bundle.getDouble("x_coord")
                    selectedLocationY = bundle.getDouble("y_coord")
                    coordinateToAddress(
                        selectedLocationX,
                        selectedLocationY,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                val handler = Handler(Looper.getMainLooper())
                                handler.post {
                                    binding.etCoordinate.editText?.setText(
                                        "%.2f, %.2f".format(
                                            selectedLocationX,
                                            selectedLocationY
                                        )
                                    )
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                if (response.isSuccessful) {
                                    val responseBody = response.body?.string()
                                    if (responseBody == null) {
                                        val handler = Handler(Looper.getMainLooper())
                                        handler.post {
                                            binding.etCoordinate.editText?.setText(
                                                "%.2f, %.2f".format(
                                                    selectedLocationX,
                                                    selectedLocationY
                                                )
                                            )
                                        }
                                    } else {
                                        var address = responseBody.subSequence(
                                            responseBody.indexOf("display_name") + 15,
                                            responseBody.length
                                        )
                                        address = address.subSequence(0, address.indexOf("\""))
                                        val handler = Handler(Looper.getMainLooper())
                                        handler.post {
                                            binding.etCoordinate.editText?.setText(address)
                                        }
                                    }
                                } else {
                                    val handler = Handler(Looper.getMainLooper())
                                    handler.post {
                                        binding.etCoordinate.editText?.setText(
                                            "%.2f, %.2f".format(
                                                selectedLocationX,
                                                selectedLocationY
                                            )
                                        )
                                    }
                                }
                            }

                        })
                }
                // to ensure it's not continuously listening the port after the location is received
                parentFragmentManager.clearFragmentResultListener("coordinatesKey")
            }
        }
    }

    private fun navigateToMapFragment() {

        mapFragment.isDialog = true // arrange that as a dialog instead of fragment
        mapFragment.coordinatesSelectedListener = this@AddEmergencyFragment
        mapFragment.show(parentFragmentManager, "mapDialog")
    }

    override fun onCoordinatesSelected(x: Double, y: Double) {
        //Log.d("YOOO x", x.toString())
        //Log.d("YOOO y", y.toString())
    }


}

private fun coordinateToAddress(x: Double, y: Double, callback: okhttp3.Callback) {
    val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    client.newCall(request).enqueue(callback)
}
