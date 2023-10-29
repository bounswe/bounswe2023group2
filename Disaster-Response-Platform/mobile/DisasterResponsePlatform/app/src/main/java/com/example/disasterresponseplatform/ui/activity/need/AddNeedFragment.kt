package com.example.disasterresponseplatform.ui.activity.need

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding

class AddNeedFragment : Fragment() {

    private lateinit var binding: FragmentAddNeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddNeedBinding.inflate(inflater, container, false)
        setUpNeedTypeSpinner()
        submitAddNeed()
        return binding.root
    }


    private fun setUpNeedTypeSpinner() {

        // Get the array of need types from needs
        val needTypeArray = resources.getStringArray(R.array.need_types)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val needTypeAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                needTypeArray
            )

        // Apply the adapter to the spinner
        val spNeedType = binding.spNeedType
        spNeedType.setAdapter(needTypeAdapter)

        spNeedType.setOnItemClickListener { _, _, position, _ ->
            // Update the data of the sub-type spinner based on the selected type
            setUpNeedSubTypeSpinner(needTypeArray[position])
        }
    }


    private fun setUpNeedSubTypeSpinner(selectedType: String) {

        // Get the array of need sub-types
        val needEmptyArray = resources.getStringArray(R.array.need_empty_sub_types)
        val needFoodArray = resources.getStringArray(R.array.need_food_sub_types)

        val needEmptyAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                needEmptyArray
            )

        val needFoodAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                needFoodArray
            )

        // Apply the adapter to the spinner
        val spNeedSubType = binding.spNeedSubType
        val boxNeedSubType = binding.boxNeedSubType

        if (selectedType.isEmpty()) {
            boxNeedSubType.visibility = View.GONE
            spNeedSubType.setText("")
            spNeedSubType.setAdapter(needEmptyAdapter)
        } else if (selectedType == "Food") {
            boxNeedSubType.visibility = View.VISIBLE
            spNeedSubType.setAdapter(needFoodAdapter)
        } else {
            boxNeedSubType.visibility = View.VISIBLE
            spNeedSubType.setText("")
            spNeedSubType.setAdapter(needEmptyAdapter)
        }
    }

    private fun submitAddNeed() {
        binding.btnSubmit.setOnClickListener {
            if ((validateFullName() and validatePhoneNumber() and validateQuantity() and validateLocation()) and validateType() and validateSubType()) {
                Toast.makeText(context, "{'created_by': ${binding.etFullName.editText?.text.toString().trim()}, 'quantity': ${binding.etQuantity.editText?.text.toString().trim()}, 'type':  ${binding.boxNeedType.editText?.text.toString().trim()}}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateType(): Boolean {
        val spNeedType = binding.spNeedType
        val boxNeedType = binding.boxNeedType

        return if (spNeedType.text.isEmpty()) {
            boxNeedType.error = "Field can not be empty"
            false
        } else {
            boxNeedType.error = null
            boxNeedType.isErrorEnabled = false
            true
        }
    }

    private fun validateSubType(): Boolean {
        val spNeedSubType = binding.spNeedSubType
        val boxNeedSubType = binding.boxNeedSubType

        return if (spNeedSubType.text.isEmpty()) {
            boxNeedSubType.error = "Field can not be empty"
            false
        } else {
            boxNeedSubType.error = null
            boxNeedSubType.isErrorEnabled = false
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
