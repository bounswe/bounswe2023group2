package com.example.disasterresponseplatform.ui.activity.resource

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.databinding.FragmentAddResourceBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.example.disasterresponseplatform.utils.DateUtil
import com.example.disasterresponseplatform.utils.StringUtil.Companion.generateRandomStringID
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AddResourceFragment(private val resourceViewModel: ResourceViewModel, private val resource: Resource?) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddResourceBinding
    private var requireActivity: FragmentActivity? = null
    private var selectedLocationX: Double? = null
    private var selectedLocationY: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Yooo on", "Damn")
        binding = FragmentAddResourceBinding.inflate(inflater, container, false)
        parentFragmentManager.setFragmentResultListener(
            "coordinatesKey",
            viewLifecycleOwner
        ) { _, bundle ->
            selectedLocationX = bundle.getDouble("x_coord")
            selectedLocationY = bundle.getDouble("y_coord")
            coordinateToAddress(selectedLocationX!!, selectedLocationY!!, object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
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
                            var address = responseBody.subSequence(responseBody.indexOf("display_name") + 15, responseBody.length)
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
        binding.etCoordinate.setEndIconOnClickListener {
            navigateToMapFragment()
        }
        setUpResourceTypeSpinner()
        fillParameters(resource)
        submitResource(resource == null)
        return binding.root
    }

    private fun navigateToMapFragment() {
        val mapFragment = ActivityMap()
        mapFragment.coordinatesSelectedListener = this@AddResourceFragment
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mapFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /** It fills the layout's fields corresponding data if it is editResource
     * It checks whether it is editResource by checking if resource is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(resource: Resource?) {
        if (resource != null) {
            binding.tvAddResource.text = getString(R.string.edit_resource)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spResourceType.setText(resource.type.toString())
            binding.spResourceSubType.setText(resource.details)
            binding.etQuantity.editText?.setText(resource.quantity.toString())

            coordinateToAddress(selectedLocationX!!, selectedLocationY!!, object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        binding.etCoordinate.editText?.setText("%.2f, %.2f".format(selectedLocationX, selectedLocationY))
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
                            var address = responseBody.subSequence(responseBody.indexOf("display_name") + 15, responseBody.length)
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
    }

    private fun setUpResourceTypeSpinner() {

        // Get the array of resource types from resource
        val resourceTypeArray = resources.getStringArray(R.array.resource_types)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val resourceTypeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resourceTypeArray
            )

        // Apply the adapter to the spinner
        val spResourceType = binding.spResourceType
        spResourceType.setAdapter(resourceTypeAdapter)

        spResourceType.setOnItemClickListener { _, _, position, _ ->
            // Update the data of the sub-type spinner based on the selected type
            setUpResourceSubTypeSpinner(resourceTypeArray[position])
        }
    }


    private fun setUpResourceSubTypeSpinner(selectedType: String) {

        // Get the array of resource sub-types
        val resourceEmptyArray = resources.getStringArray(R.array.resource_empty_sub_types)
        val resourceFoodArray = resources.getStringArray(R.array.resource_food_sub_types)

        val resourceEmptyAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resourceEmptyArray
            )

        val resourceFoodAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resourceFoodArray
            )

        // Apply the adapter to the spinner
        val spResourceSubType = binding.spResourceSubType
        val boxResourceSubType = binding.boxResourceSubType

        if (selectedType.isEmpty()) {
            spResourceSubType.setText("")
            spResourceSubType.setAdapter(resourceEmptyAdapter)
        } else if (selectedType == "Food") {
            spResourceSubType.setAdapter(resourceFoodAdapter)
        } else {
            spResourceSubType.setText("")
            spResourceSubType.setAdapter(resourceEmptyAdapter)
        }
    }

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitResource(isAdd: Boolean) {
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            if (validateQuantity() and validateCoordinateY() and validateCoordinateX() and validateType() and validateSubType()) {

                val type: NeedTypes =
                    when (binding.boxResourceType.editText?.text.toString().trim()) {
                        NeedTypes.Cloth.toString() -> NeedTypes.Cloth
                        NeedTypes.Food.toString() -> NeedTypes.Food
                        NeedTypes.Drink.toString() -> NeedTypes.Drink
                        NeedTypes.Shelter.toString() -> NeedTypes.Shelter
                        NeedTypes.Medication.toString() -> NeedTypes.Medication
                        NeedTypes.Transportation.toString() -> NeedTypes.Transportation
                        NeedTypes.Tools.toString() -> NeedTypes.Tools
                        NeedTypes.Human.toString() -> NeedTypes.Human
                        else -> NeedTypes.Other
                    }
                val creatorName = DiskStorageManager.getKeyValue("username").toString()
                val details = binding.spResourceSubType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val newResource: Resource
                if (selectedLocationX == null || selectedLocationY == null) {
                    selectedLocationX = 0.0
                    selectedLocationY = 0.0
                    Toast.makeText(requireContext(), "Location is not selected", Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                } else {
                    newResource = Resource(
                        generateRandomStringID(),
                        creatorName,
                        "new",
                        quantity,
                        type,
                        details,
                        date,
                        selectedLocationX!!,
                        selectedLocationY!!
                    )
                }

                //resourceViewModel.insertResource(resource) insert local db
                if (isAdd) {
                    resourceViewModel.postResourceRequest(newResource)
                } else {
                    val resourceID = "/" + resource!!.ID // comes from older resource
                    resourceViewModel.postResourceRequest(newResource, resourceID)
                }
                resourceViewModel.getLiveDataResourceID().observe(requireActivity!!) {
                    if (isAdded) { // to ensure it attached a context
                        if (isAdd)
                            Toast.makeText(
                                requireContext(),
                                "Created Resource ID: $it",
                                Toast.LENGTH_LONG
                            ).show()
                        else
                            Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT).show()
                    }

                    Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                        if (isAdded) // to ensure it attached a parentFragmentManager
                            parentFragmentManager.popBackStack(
                                "AddResourceFragment",
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )
                        if (!isAdd) {
                            parentFragmentManager.popBackStack(
                                "ResourceItemFragment",
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )
                        }
                        // Re-enable the button after the background operation completes
                        binding.btnSubmit.isEnabled = true
                    }, 200)
                }
            } else {
                Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun validateType(): Boolean {
        val spResourceType = binding.spResourceType
        val boxResourceType = binding.boxResourceType

        return if (spResourceType.text.isEmpty()) {
            boxResourceType.error = "Field can not be empty"
            false
        } else {
            boxResourceType.error = null
            boxResourceType.isErrorEnabled = false
            true
        }
    }

    private fun validateSubType(): Boolean {
        val spResourceSubType = binding.spResourceSubType
        val boxResourceSubType = binding.boxResourceSubType

        return if (spResourceSubType.text.isEmpty()) {
            boxResourceSubType.error = "Field can not be empty"
            false
        } else {
            boxResourceSubType.error = null
            boxResourceSubType.isErrorEnabled = false
            true
        }
    }


    private fun validateCoordinateX(): Boolean {
        return if (selectedLocationX == null) {
            binding.etCoordinate.error = "Field can not be empty"
            false
        } else {
            binding.etCoordinate.error = null
            binding.etCoordinate.isErrorEnabled = false
            true
        }
    }

    private fun validateCoordinateY(): Boolean {
        return if (selectedLocationY == null) {
            binding.etCoordinate.error = "Field can not be empty"
            false
        } else {
            binding.etCoordinate.error = null
            binding.etCoordinate.isErrorEnabled = false
            true
        }
    }

    private fun validateQuantity(): Boolean {
        val etQuantity = binding.etQuantity
        val quantity = etQuantity.editText?.text.toString().trim()

        return if (quantity.isEmpty()) {
            etQuantity.error = "Field can not be empty"
            false
        } else {
            etQuantity.error = null
            etQuantity.isErrorEnabled = false
            true
        }
    }

    override fun onCoordinatesSelected(x: Double, y: Double) {
        //Log.d("YOOO x", x.toString())
        //Log.d("YOOO y", y.toString())
        // binding.etCoordinateY.editText?.setText("Test String")
        //requireActivity().runOnUiThread {
        //binding.etCoordinateX.editText!!.setText(String.format("%.2f", x).replace(',', '.'))
        //binding.etCoordinateY.editText!!.setText(String.format("%.2f", y).replace(',', '.'))
        //}
    }
}


private fun coordinateToAddress(x: Double, y: Double, callback: Callback) {
    val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    client.newCall(request).enqueue(callback)
}
