package com.example.disasterresponseplatform.ui.activity.action

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.ActionBody
import com.example.disasterresponseplatform.databinding.FragmentAddActionBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.example.disasterresponseplatform.utils.Annotation
import com.example.disasterresponseplatform.utils.DateUtil.Companion.dateForBackend
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Calendar

class AddActionFragment(
    private val actionViewModel: ActionViewModel,
    private val action: ActionBody.ActionItem?
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddActionBinding
    private var requireActivity: FragmentActivity? = null
    private var annotation = Annotation()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddActionBinding.inflate(inflater, container, false)
        fillParameters(action)
        var occurAt = binding.etOccurAt
        occurAt.setOnClickListener{showDatePickerDialog(occurAt)}
        var endAt = binding.etEndAt
        endAt.setOnClickListener{showDatePickerDialog(endAt)}
        submitAction(action == null)
        return binding.root
    }

    /** It fills the layout's fields corresponding data if it is editAction
     * It checks whether it is editAction by checking if action is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(action: ActionBody.ActionItem?) {
        if (action != null) {
            binding.tvAddAction.text = getString(R.string.edit_action)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spActionType.setText(action.type)
        }
    }

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitAction(isAdd: Boolean) {
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            val layAddAction = binding.layAddAction
            if (validateFields(layAddAction)) {

                val type2 = binding.spActionType.text.toString().trim()
                val occurAt = binding.etOccurAt.text.toString()
                val endAt = binding.etEndAt.text.toString()
                val description = binding.etDesc.text.toString()
                val related_need = binding.etRelatedNeedID.text.toString()
                val related_resource = binding.etRelatedResourceID.text.toString()
                val relatedGroup1 = ActionBody.RelatedGroup(
                    recurrence = true,
                    groupType = "ExampleType",
                    relatedNeeds = listOf(related_need),
                    relatedResources = listOf(related_resource)
                )

                annotation.publishAnnotation(DiskStorageManager.getKeyValue("username")!! + "-action-" + type2, description)
                val actionPost = ActionBody.ActionRequestBody(
                    description, type2, occurAt, endAt, listOf(relatedGroup1)
                )

                if (isAdd) {
                    actionViewModel.postActionRequest(actionPost)
                } else {
                    val actionID = "/" + action!!._id // comes from older action
                    actionViewModel.postActionRequest(actionPost, actionID)
                }
                actionViewModel.getLiveDataActionID().observe(requireActivity!!) {
                    if (it != "-1") { // in error cases it returns this
                        if (isAdded) { // to ensure it attached a context
                            if (isAdd)
                                Toast.makeText(
                                    requireContext(),
                                    "Created Action ID: $it",
                                    Toast.LENGTH_LONG
                                ).show()
                            else
                                Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT)
                                    .show()
                        }

                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                            if (isAdded) // to ensure it attached a parentFragmentManager
                                parentFragmentManager.popBackStack(
                                    "AddActionFragment",
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )
                            if (!isAdd)
                                parentFragmentManager.popBackStack(
                                    "ActionItemFragment",
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )
                            // Re-enable the button after the background operation completes
                            binding.btnSubmit.isEnabled = true
                        }, 200)
                    } else {
                        if (isAdded)
                            Toast.makeText(
                                requireContext(),
                                "Error Check Logs",
                                Toast.LENGTH_SHORT
                            )
                                .show()
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

    /**
     * Utility function for picking date
     */
    private fun showDatePickerDialog(textInputEditText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                textInputEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onCoordinatesSelected(x: Double, y: Double) {
        TODO("Not yet implemented")
    }
}


