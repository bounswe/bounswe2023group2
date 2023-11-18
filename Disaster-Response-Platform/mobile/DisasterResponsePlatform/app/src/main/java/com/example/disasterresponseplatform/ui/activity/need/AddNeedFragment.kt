package com.example.disasterresponseplatform.ui.activity.need

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
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding
import com.example.disasterresponseplatform.utils.DateUtil
import com.example.disasterresponseplatform.utils.StringUtil

class AddNeedFragment(private val needViewModel: NeedViewModel) : Fragment() {

    private lateinit var binding: FragmentAddNeedBinding
    private var requireActivity: FragmentActivity? = null

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
            spNeedSubType.setText("")
            spNeedSubType.setAdapter(needEmptyAdapter)
        } else if (selectedType == "Food") {
            spNeedSubType.setAdapter(needFoodAdapter)
        } else {
            spNeedSubType.setText("")
            spNeedSubType.setAdapter(needEmptyAdapter)
        }
    }

    private fun submitAddNeed() {
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            if (validateFullName() and validateQuantity() and validateCoordinateX() and validateCoordinateY()and validateType() and validateSubType()) {
                Toast.makeText(context, "{'created_by': ${binding.etFullName.editText?.text.toString().trim()}, 'quantity': ${binding.etQuantity.editText?.text.toString().trim()}, 'type':  ${binding.boxNeedType.editText?.text.toString().trim()}}", Toast.LENGTH_SHORT).show()

                val type: NeedTypes =
                when(binding.boxNeedType.editText?.text.toString().trim()){
                    NeedTypes.Clothes.toString() -> NeedTypes.Clothes
                    NeedTypes.Food.toString() -> NeedTypes.Food
                    NeedTypes.Shelter.toString() -> NeedTypes.Shelter
                    NeedTypes.Medication.toString() ->NeedTypes.Medication
                    NeedTypes.Transportation.toString() ->NeedTypes.Transportation
                    NeedTypes.Tools.toString() -> NeedTypes.Tools
                    NeedTypes.Human.toString() -> NeedTypes.Human
                    else -> NeedTypes.Other
                }
                val creatorName = binding.etFullName.editText?.text.toString().trim()
                val details = binding.spNeedSubType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val coordinateX = binding.etCoordinateX.editText?.text.toString().trim().toDouble()
                val coordinateY = binding.etCoordinateY.editText?.text.toString().trim().toDouble()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val need = Need(StringUtil.generateRandomStringID(),creatorName,type,details, date,quantity, coordinateX,coordinateY,1)

                //needViewModel.insertNeed(need)
                needViewModel.postNeedRequest(need)
                needViewModel.getLiveDataResourceID().observe(requireActivity!!){
                    if (it != "null"){ // when it's not empty or null
                        Toast.makeText(context, "Created Resource ID: $it", Toast.LENGTH_LONG).show()
                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                            parentFragmentManager.popBackStack()
                            binding.btnSubmit.isEnabled = true
                        }, 200)
                    }
                }
            } else {
                Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
                binding.btnSubmit.isEnabled = true
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
