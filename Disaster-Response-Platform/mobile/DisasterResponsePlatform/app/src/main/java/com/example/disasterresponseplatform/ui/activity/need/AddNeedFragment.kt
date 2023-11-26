package com.example.disasterresponseplatform.ui.activity.need

import android.annotation.SuppressLint
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
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.utils.DateUtil
import com.example.disasterresponseplatform.utils.StringUtil
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AddNeedFragment(private val needViewModel: NeedViewModel, private val need: Need?) : Fragment() {

    private lateinit var binding: FragmentAddNeedBinding
    private var requireActivity: FragmentActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddNeedBinding.inflate(inflater, container, false)
        fillParameters(need)
        submitNeed(need == null)

        val networkManager = NetworkManager()
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )

        networkManager.makeRequest(
            endpoint = Endpoint.FORM_FIELDS_NEED,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val formFieldsNeedResponse = gson.fromJson(
                                    rawJson,
                                    NeedBody.NeedFormFieldsResponse::class.java
                                )
                                if (formFieldsNeedResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsNeedResponse: $formFieldsNeedResponse"
                                    )

                                    val othersLayout = binding.layOthers
                                    val fields: List<NeedBody.NeedFormFields> =
                                        formFieldsNeedResponse.fields

                                    for (field in fields) {
                                        val name = field.name
                                        val label = field.label
                                        val fieldType = field.type

                                        when (fieldType) {

                                            "text" -> {
                                                val textInputLayout = TextInputLayout(
                                                    requireContext(),
                                                    null,
                                                    R.attr.customTextInputStyle
                                                )
                                                textInputLayout.hint = label
                                                textInputLayout.isEndIconVisible = true
                                                textInputLayout.endIconMode =
                                                    TextInputLayout.END_ICON_CLEAR_TEXT
                                                textInputLayout.layoutParams =
                                                    LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    )

                                                val textInputEditText =
                                                    TextInputEditText(textInputLayout.context)
                                                textInputEditText.inputType =
                                                    InputType.TYPE_CLASS_TEXT

                                                textInputLayout.addView(textInputEditText)
                                                othersLayout.addView(textInputLayout)
                                            }

                                            "number" -> {
                                                if (name == "x" || name == "y" || name == "initialQuantity" || name == "unsuppliedQuantity") {
                                                    // Pass
                                                } else {
                                                    val textInputLayout = TextInputLayout(
                                                        requireContext(),
                                                        null,
                                                        R.attr.customTextInputStyle
                                                    )
                                                    textInputLayout.hint = label
                                                    textInputLayout.isEndIconVisible = true
                                                    textInputLayout.endIconMode =
                                                        TextInputLayout.END_ICON_CLEAR_TEXT
                                                    textInputLayout.layoutParams =
                                                        LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        )

                                                    val textInputEditText =
                                                        TextInputEditText(textInputLayout.context)
                                                    textInputEditText.inputType =
                                                        InputType.TYPE_CLASS_NUMBER

                                                    textInputLayout.addView(textInputEditText)
                                                    othersLayout.addView(textInputLayout)
                                                }

                                            }

                                            "date" -> {
                                                val textInputLayout = TextInputLayout(
                                                    requireContext(),
                                                    null,
                                                    R.attr.customTextInputStyle
                                                )
                                                textInputLayout.hint = label
                                                textInputLayout.isEndIconVisible = true
                                                textInputLayout.endIconMode =
                                                    TextInputLayout.END_ICON_CLEAR_TEXT
                                                textInputLayout.layoutParams =
                                                    LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    )

                                                val textInputEditText =
                                                    TextInputEditText(textInputLayout.context)
                                                textInputEditText.inputType =
                                                    InputType.TYPE_DATETIME_VARIATION_DATE

                                                textInputLayout.addView(textInputEditText)
                                                othersLayout.addView(textInputLayout)
                                            }

                                            "select" -> {

                                                if (name == "type") {
                                                    // Create Spinner for select type
                                                    val options = field.options ?: emptyList()
                                                    val optionsAdapter = ArrayAdapter(
                                                        requireContext(),
                                                        android.R.layout.simple_dropdown_item_1line,
                                                        options
                                                    )
                                                    val spNeedType = binding.spNeedType
                                                    spNeedType.setAdapter(optionsAdapter)

                                                } else {
                                                    val textInputLayout = TextInputLayout(
                                                        requireContext(),
                                                        null,
                                                        R.attr.customDropDownStyle
                                                    )
                                                    textInputLayout.hint = label
                                                    textInputLayout.isEndIconVisible = true
                                                    textInputLayout.endIconMode =
                                                        TextInputLayout.END_ICON_DROPDOWN_MENU
                                                    textInputLayout.layoutParams =
                                                        LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        )

                                                    val materialAutoCompleteTextView =
                                                        MaterialAutoCompleteTextView(textInputLayout.context)
                                                    materialAutoCompleteTextView.inputType =
                                                        InputType.TYPE_CLASS_TEXT

                                                    textInputLayout.addView(
                                                        materialAutoCompleteTextView
                                                    )
                                                    othersLayout.addView(textInputLayout)

                                                    // Create Spinner for select type
                                                    val options = field.options ?: emptyList()
                                                    val optionsAdapter = ArrayAdapter(
                                                        requireContext(),
                                                        android.R.layout.simple_dropdown_item_1line,
                                                        options
                                                    )
                                                    materialAutoCompleteTextView.setAdapter(
                                                        optionsAdapter
                                                    )
                                                }
                                            }
                                        }

                                    }

                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseSuccess", "Body: $errorBody")
                        }
                    }
                }
            }
        )

        // Listener for Type field
        val spNeedType = binding.spNeedType
        spNeedType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called when the text is changing
                val newText = s.toString()
                // Perform actions based on the changing text
                // For example, update a list based on the entered text
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has changed
                val selectedType = spNeedType.text.toString()
                typeChanged(selectedType)
            }
        })

        return binding.root
    }


    /**
     * This function triggered when Type field changed.
     * Makes api call to retrieve type-specific form fields of selected type.
     * Creates type-specific form fields.
     * Adds these fields into laySpecific layout that holds type-specific fields such as allergens, expiration_date.
     * Creates subtype adapter and connects it.
     */
    private fun typeChanged(selectedType: String) {

        // Delete all type-specific fields
        val specificLayout = binding.laySpecific
        specificLayout.removeAllViews()

        // Clear subType field and detach its adapter
        val spNeedSubType = binding.spNeedSubType
        spNeedSubType.text.clear()
        spNeedSubType.setAdapter(null)

        // Find type-specific fields and subType list
        val networkManager = NetworkManager()
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )

        networkManager.makeRequest(
            endpoint = Endpoint.FORM_FIELDS_TYPE,
            requestType = RequestType.GET,
            id = selectedType,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val formFieldsNeedResponse = gson.fromJson(
                                    rawJson,
                                    NeedBody.NeedFormFieldsResponse::class.java
                                )
                                if (formFieldsNeedResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsNeedResponse: $formFieldsNeedResponse"
                                    )

                                    val specificLayout = binding.laySpecific
                                    val fields: List<NeedBody.NeedFormFields> =
                                        formFieldsNeedResponse.fields

                                    for (field in fields) {
                                        val name = field.name
                                        val label = field.label
                                        val fieldType = field.type

                                        when (fieldType) {

                                            "text" -> {
                                                val textInputLayout = TextInputLayout(
                                                    requireContext(),
                                                    null,
                                                    R.attr.customTextInputStyle
                                                )
                                                textInputLayout.hint = label
                                                textInputLayout.isEndIconVisible = true
                                                textInputLayout.endIconMode =
                                                    TextInputLayout.END_ICON_CLEAR_TEXT
                                                textInputLayout.layoutParams =
                                                    LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    )

                                                val textInputEditText =
                                                    TextInputEditText(textInputLayout.context)
                                                textInputEditText.inputType =
                                                    InputType.TYPE_CLASS_TEXT

                                                textInputLayout.addView(textInputEditText)
                                                specificLayout.addView(textInputLayout)
                                            }

                                            "number" -> {
                                                val textInputLayout = TextInputLayout(
                                                    requireContext(),
                                                    null,
                                                    R.attr.customTextInputStyle
                                                )
                                                textInputLayout.hint = label
                                                textInputLayout.isEndIconVisible = true
                                                textInputLayout.endIconMode =
                                                    TextInputLayout.END_ICON_CLEAR_TEXT
                                                textInputLayout.layoutParams =
                                                    LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    )

                                                val textInputEditText =
                                                    TextInputEditText(textInputLayout.context)
                                                textInputEditText.inputType =
                                                    InputType.TYPE_CLASS_NUMBER

                                                textInputLayout.addView(textInputEditText)
                                                specificLayout.addView(textInputLayout)
                                            }

                                            "date" -> {
                                                val textInputLayout = TextInputLayout(
                                                    requireContext(),
                                                    null,
                                                    R.attr.customTextInputStyle
                                                )
                                                textInputLayout.hint = label
                                                textInputLayout.isEndIconVisible = true
                                                textInputLayout.endIconMode =
                                                    TextInputLayout.END_ICON_CLEAR_TEXT
                                                textInputLayout.layoutParams =
                                                    LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    )

                                                val textInputEditText =
                                                    TextInputEditText(textInputLayout.context)
                                                textInputEditText.inputType =
                                                    InputType.TYPE_DATETIME_VARIATION_DATE

                                                textInputLayout.addView(textInputEditText)
                                                specificLayout.addView(textInputLayout)
                                            }

                                            "select" -> {

                                                if (name == "subtype") {
                                                    // Create Spinner for select subtype
                                                    val options = field.options ?: emptyList()
                                                    val optionsAdapter = ArrayAdapter(
                                                        requireContext(),
                                                        android.R.layout.simple_dropdown_item_1line,
                                                        options
                                                    )
                                                    spNeedSubType.setAdapter(optionsAdapter)

                                                } else {
                                                    val textInputLayout = TextInputLayout(
                                                        requireContext(),
                                                        null,
                                                        R.attr.customDropDownStyle
                                                    )
                                                    textInputLayout.hint = label
                                                    textInputLayout.isEndIconVisible = true
                                                    textInputLayout.endIconMode =
                                                        TextInputLayout.END_ICON_DROPDOWN_MENU
                                                    textInputLayout.layoutParams =
                                                        LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        )

                                                    val materialAutoCompleteTextView =
                                                        MaterialAutoCompleteTextView(textInputLayout.context)
                                                    materialAutoCompleteTextView.inputType =
                                                        InputType.TYPE_CLASS_TEXT

                                                    textInputLayout.addView(
                                                        materialAutoCompleteTextView
                                                    )
                                                    specificLayout.addView(textInputLayout)


                                                    // Create Spinner for select type
                                                    val options = field.options ?: emptyList()
                                                    val optionsAdapter = ArrayAdapter(
                                                        requireContext(),
                                                        android.R.layout.simple_dropdown_item_1line,
                                                        options
                                                    )
                                                    materialAutoCompleteTextView.setAdapter(
                                                        optionsAdapter
                                                    )
                                                }
                                            }
                                        }

                                    }

                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseSuccess", "Body: $errorBody")
                        }
                    }
                }
            }
        )

    }


    /** It fills the layout's fields corresponding data if it is editNeed
     * It checks whether it is editNeed by checking if need is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(need: Need?) {
        if (need != null) {
            binding.tvAddNeed.text = getString(R.string.edit_need)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spNeedType.setText(need.type.toString())
            binding.spNeedSubType.setText(need.details)
            binding.etQuantity.editText?.setText(need.quantity.toString())
            binding.etCoordinateX.editText?.setText(
                String.format("%.2f", need.coordinateX).replace(',', '.')
            )
            binding.etCoordinateY.editText?.setText(
                String.format("%.2f", need.coordinateY).replace(',', '.')
            )
        }
    }


    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitNeed(isAdd: Boolean) {
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            val layAddNeed = binding.layAddNeed
            if (validateFields(layAddNeed)) {

                val type: NeedTypes =
                    when (binding.boxNeedType.editText?.text.toString().trim()) {
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
                val details = binding.spNeedSubType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val coordinateX = binding.etCoordinateX.editText?.text.toString().trim().toDouble()
                val coordinateY = binding.etCoordinateY.editText?.text.toString().trim().toDouble()
                val date = DateUtil.getDate("dd-MM-yy").toString()
                val newNeed = Need(
                    StringUtil.generateRandomStringID(),
                    creatorName,
                    type,
                    details,
                    date,
                    quantity,
                    coordinateX,
                    coordinateY,
                    1
                )

                //needViewModel.insertNeed(need) insert local db
                if (isAdd) {
                    needViewModel.postNeedRequest(newNeed)
                } else {
                    val needID = "/" + need!!.ID // comes from older need
                    needViewModel.postNeedRequest(newNeed, needID)
                }
                needViewModel.getLiveDataNeedID().observe(requireActivity!!) {
                    if (it != "-1") { // in error cases it returns this
                        if (isAdded) { // to ensure it attached a context
                            if (isAdd)
                                Toast.makeText(
                                    requireContext(),
                                    "Created Need ID: $it",
                                    Toast.LENGTH_LONG
                                ).show()
                            else
                                Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT)
                                    .show()
                        }

                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                            if (isAdded) // to ensure it attached a parentFragmentManager
                                parentFragmentManager.popBackStack(
                                    "AddNeedFragment",
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )
                            if (!isAdd)
                                parentFragmentManager.popBackStack(
                                    "NeedItemFragment",
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                                )
                            // Re-enable the button after the background operation completes
                            binding.btnSubmit.isEnabled = true
                        }, 200)
                    } else {
                        Toast.makeText(requireContext(), "Error Check Logs", Toast.LENGTH_SHORT)
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

            // If there are nested layouts, recursively check their children
            if (view is ViewGroup) {
                isValid = isValid && validateFields(view)
            }
        }
        return isValid
    }
}
