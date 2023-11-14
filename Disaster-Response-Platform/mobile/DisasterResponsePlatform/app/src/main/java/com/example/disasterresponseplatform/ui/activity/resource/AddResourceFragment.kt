package com.example.disasterresponseplatform.ui.activity.resource

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.databinding.FragmentAddResourceBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.utils.DateUtil

class AddResourceFragment(private val resourceViewModel: ResourceViewModel, private val resource: Resource?) : Fragment() {

    private lateinit var binding: FragmentAddResourceBinding
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddResourceBinding.inflate(inflater, container, false)
        setUpResourceTypeSpinner()
        fillParameters(resource)
        submitResource(resource == null)
        return binding.root
    }

    /** It fills the layout's fields corresponding data if it is editResource
     * It checks whether it is editResource by checking if resource is null, if it is not null then it should be edit form
     */
    private fun fillParameters(resource: Resource?){
        if (resource != null){
            binding.spResourceType.setText(resource.type.toString())
            binding.spResourceSubType.setText(resource.details)
            binding.etQuantity.editText?.setText(resource.quantity.toString())
            binding.etCoordinateX.editText?.setText(resource.coordinateX.toString())
            binding.etCoordinateY.editText?.setText(resource.coordinateY.toString())
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
            boxResourceSubType.visibility = View.GONE
            spResourceSubType.setText("")
            spResourceSubType.setAdapter(resourceEmptyAdapter)
        } else if (selectedType == "Food") {
            boxResourceSubType.visibility = View.VISIBLE
            spResourceSubType.setAdapter(resourceFoodAdapter)
        } else {
            boxResourceSubType.visibility = View.VISIBLE
            spResourceSubType.setText("")
            spResourceSubType.setAdapter(resourceEmptyAdapter)
        }
    }

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitResource(isAdd : Boolean) {
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        binding.btnSubmit.setOnClickListener {
            if (validateQuantity() and validateCoordinateY() and validateCoordinateX()  and validateType() and validateSubType()) {

                val type: NeedTypes =
                    when(binding.boxResourceType.editText?.text.toString().trim()){
                        NeedTypes.Clothes.toString() -> NeedTypes.Clothes
                        NeedTypes.Food.toString() -> NeedTypes.Food
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
                val coordinateX = binding.etCoordinateX.editText?.text.toString().trim().toDouble()
                val coordinateY = binding.etCoordinateY.editText?.text.toString().trim().toDouble()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val resource = Resource(null,creatorName,"new",quantity,type,details,date,coordinateX,coordinateY)

                //resourceViewModel.insertResource(resource) insert local db
                if (isAdd){
                    resourceViewModel.postResourceRequest(resource,RequestType.POST)
                } else{
                    resourceViewModel.postResourceRequest(resource,RequestType.PUT)
                }
                resourceViewModel.getLiveDataResourceID().observe(requireActivity!!){
                    if (isAdd){
                        Toast.makeText(context, "Created Resource ID: $it", Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(context, "Updated Resource ID: $it", Toast.LENGTH_LONG).show()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                        parentFragmentManager.popBackStack()
                    }, 200)
                }
            } else {
                Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
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
        val etCoordinateX = binding.etCoordinateX
        val coordinateX = etCoordinateX.editText?.text.toString().trim()
        return if (coordinateX.isEmpty()) {
            etCoordinateX.error = "Field can not be empty"
            false
        } else {
            etCoordinateX.error = null
            etCoordinateX.isErrorEnabled = false
            true
        }
    }

    private fun validateCoordinateY(): Boolean {
        val etCoordinateY = binding.etCoordinateY
        val coordinateY = etCoordinateY.editText?.text.toString().trim()
        return if (coordinateY.isEmpty()) {
            etCoordinateY.error = "Field can not be empty"
            false
        } else {
            etCoordinateY.error = null
            etCoordinateY.isErrorEnabled = false
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
}
