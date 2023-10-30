package com.example.disasterresponseplatform.ui.activity.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.databinding.FragmentAddResourceBinding
import com.example.disasterresponseplatform.utils.DateUtil

class AddResourceFragment(private val resourceViewModel: ResourceViewModel) : Fragment() {

    private lateinit var binding: FragmentAddResourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddResourceBinding.inflate(inflater, container, false)
        setUpResourceTypeSpinner()
        submitAddResource()
        return binding.root
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

    private fun submitAddResource() {
        binding.btnSubmit.setOnClickListener {
            if ((validateFullName() and validatePhoneNumber() and validateQuantity() and validateLocation()) and validateType() and validateSubType()) {
                Toast.makeText(context, "{'created_by': ${binding.etFullName.editText?.text.toString().trim()}, 'quantity': ${binding.etQuantity.editText?.text.toString().trim()}, 'type':  ${binding.boxResourceType.editText?.text.toString().trim()}}", Toast.LENGTH_SHORT).show()

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
                val creatorName = binding.etFullName.editText?.text.toString().trim()
                val details = binding.spResourceSubType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val location = binding.etLocation.editText?.text.toString().trim()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val resource = Resource(null,creatorName,"new",quantity,type,details,date,location)
                //TODO do with token
                resourceViewModel.insertResource(resource)
                parentFragmentManager.popBackStack()
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


    private fun validateLocation(): Boolean {
        val etLocation = binding.etLocation
        val location = etLocation.editText?.text.toString().trim()

        return if (location.isEmpty()) {
            etLocation.error = "Field can not be empty"
            false
        } else {
            etLocation.error = null
            etLocation.isErrorEnabled = false
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

    private fun validateFullName(): Boolean {
        val etFullName = binding.etFullName
        val fullName = etFullName.editText?.text.toString().trim()

        return if (fullName.isEmpty()) {
            etFullName.error = "Field can not be empty"
            false
        } else {
            etFullName.error = null
            etFullName.isErrorEnabled = false
            true
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val etPhoneNumber = binding.etPhoneNumber
        val phoneNumber = etPhoneNumber.editText?.text.toString().trim()

        return if (phoneNumber.isEmpty()) {
            etPhoneNumber.error = "Field can not be empty$phoneNumber"
            false
        } else {
            etPhoneNumber.error = null
            etPhoneNumber.isErrorEnabled = false
            true
        }
    }
}
