package com.example.disasterresponseplatform.ui.activity.need

import android.annotation.SuppressLint
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
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.utils.DateUtil
import com.example.disasterresponseplatform.utils.StringUtil

class AddNeedFragment(private val needViewModel: NeedViewModel, private val need: Need?) : Fragment() {

    private lateinit var binding: FragmentAddNeedBinding
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddNeedBinding.inflate(inflater, container, false)
        setUpNeedTypeSpinner()
        fillParameters(need)
        submitNeed(need == null)
        return binding.root
    }


    /** It fills the layout's fields corresponding data if it is editNeed
     * It checks whether it is editNeed by checking if need is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(need: Need?){
        if (need != null){
            binding.tvAddNeed.text = getString(R.string.edit_need)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spNeedType.setText(need.type.toString())
            binding.spNeedSubType.setText(need.details)
            binding.etQuantity.editText?.setText(need.quantity.toString())
            binding.etCoordinateX.editText?.setText(String.format("%.2f", need.coordinateX).replace(',', '.'))
            binding.etCoordinateY.editText?.setText(String.format("%.2f", need.coordinateY).replace(',', '.'))
        }
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

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitNeed(isAdd : Boolean) {
        if (requireActivity == null){ // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            if (validateQuantity() and validateCoordinateX() and validateCoordinateY()and validateType() and validateSubType()) {

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
                val creatorName = DiskStorageManager.getKeyValue("username").toString()
                val details = binding.spNeedSubType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val coordinateX = binding.etCoordinateX.editText?.text.toString().trim().toDouble()
                val coordinateY = binding.etCoordinateY.editText?.text.toString().trim().toDouble()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val newNeed = Need(StringUtil.generateRandomStringID(),creatorName,type, details, date,quantity, coordinateX,coordinateY,1)

                //needViewModel.insertNeed(need) insert local db
                if (isAdd){
                    needViewModel.postNeedRequest(newNeed)
                } else {
                    val needID = "/"+need!!.ID // comes from older need
                    needViewModel.postNeedRequest(newNeed,needID)
                }
                needViewModel.getLiveDataNeedID().observe(requireActivity!!){
                    if (it != "-1"){ // in error cases it returns this
                        if (isAdded){ // to ensure it attached a context
                            if (isAdd)
                                Toast.makeText(requireContext(), "Created Need ID: $it", Toast.LENGTH_LONG).show()
                            else
                                Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT).show()
                        }

                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                            if (isAdded) // to ensure it attached a parentFragmentManager
                                parentFragmentManager.popBackStack("AddNeedFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            if (!isAdd)
                                parentFragmentManager.popBackStack("NeedItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            // Re-enable the button after the background operation completes
                            binding.btnSubmit.isEnabled = true
                        }, 200)
                    } else{
                        Toast.makeText(requireContext(), "Error Check Logs", Toast.LENGTH_SHORT).show()
                        binding.btnSubmit.isEnabled = true
                    }
                }
            } else {
                if (isAdded)
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

}
