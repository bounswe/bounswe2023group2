package com.example.disasterresponseplatform.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.FragmentAddNoInternetFormBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel

/**
 * This class is opened when device has no internet connection, therefore its form fields
 * are hardcoded. Therefore the form can not contain any specific fields.
 * @param viewModel is for keeping the viewModel, its actual child type should be change to the aim
 * i.e if this form will open for Add Need without connection viewModel should be ViewModel
 * The specific things for Need will be applied with respect to that viewModel type
 */
class AddNoInternetFormFragment(private val viewModel: ViewModel) : Fragment() {

    private lateinit var binding: FragmentAddNoInternetFormBinding
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNoInternetFormBinding.inflate(inflater, container, false)
        if (requireActivity == null){ requireActivity = requireActivity()}
        arrangeUI()
        binding.btnSubmit.setOnClickListener {
            saveInLocal()
        }
        return binding.root
    }

    /**
     * This function is used for arranging the UI, if it's event or need UI should be changed
     * respectively
     */
    private fun arrangeUI(){
        when (viewModel) {
            is EventViewModel -> {
                binding.tvQuantity.isVisible = false
                // arrange split items type
                val eventTypesList: List<String> = resources.getStringArray(R.array.event_types).toList()
                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    eventTypesList
                )
                binding.spType.setAdapter(adapter)
            }

            is NeedViewModel -> {
                binding.tvAddEvent.text = getString(R.string.add_need)
                binding.boxType.hint = getString(R.string.field_need_type)
                val eventTypesList: List<String> = resources.getStringArray(R.array.need_types).toList()
                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    eventTypesList
                )
                binding.spType.setAdapter(adapter)
            }

            is ResourceViewModel -> {
                binding.tvAddEvent.text = getString(R.string.add_resource)
                binding.boxType.hint = getString(R.string.field_resource_type)
                val eventTypesList: List<String> = resources.getStringArray(R.array.resource_types).toList()
                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    eventTypesList
                )
                binding.spType.setAdapter(adapter)
            }
            else -> {}
        }
    }

    /**
     * This function insert entities in local database when user clicked save button
     * and required fields are filled
     */
    private fun saveInLocal(){
        val username = DiskStorageManager.getKeyValue("username")!!
        if (validateFields()){
            val shortDescription = binding.etShortDescription.text!!.toString().trim()
            val additionalNotes = binding.etNotes.text!!.toString().trim()
            val address = binding.etAddress.text!!.toString().trim()
            val type = binding.spType.text!!.toString().trim()
            when (viewModel){
                is EventViewModel -> {
                    val savedEvent = Event(null,username,type,shortDescription,additionalNotes,address)
                    viewModel.insertEvent(savedEvent)
                }
                is NeedViewModel -> {
                    val quantity =
                        if (binding.etQuantity.text.isNullOrEmpty()) 1
                        else binding.etQuantity.text!!.toString().trim().toInt()
                    val savedNeed = Need(null,username,type,shortDescription,additionalNotes,quantity,address)
                    viewModel.insertNeed(savedNeed)
                }
                is ResourceViewModel -> {
                    val quantity =
                        if (binding.etQuantity.text.isNullOrEmpty()) 1
                        else binding.etQuantity.text!!.toString().trim().toInt()
                    val savedResource = Resource(null,username,type,shortDescription,additionalNotes,quantity,address)
                    viewModel.insertResource(savedResource)
                }
                else -> {}
            }
            if (isAdded) { // to ensure it attached a context
                Toast.makeText(requireContext(), "It is saved in Local, It will be uploaded when you connect to the internet", Toast.LENGTH_LONG).show()
            }
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * Users must enter event type, location and short description
     */
    private fun validateFields(): Boolean {
        var returnVal = true
        if (binding.etNotes.text.isNullOrEmpty()){
            binding.tvNotes.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.etShortDescription.text.isNullOrEmpty()){
            binding.tvShortDescription.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.spType.text.isNullOrEmpty()){
            binding.boxType.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.etAddress.text.isNullOrEmpty()){
            binding.tvAddress.error = "Cannot be empty"
            returnVal = false
        }
        return returnVal
    }





}