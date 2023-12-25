package com.example.disasterresponseplatform.ui.activity.need

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
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.databinding.FragmentAddNeedBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.example.disasterresponseplatform.utils.DateUtil.Companion.dateForBackend
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Calendar
import kotlin.properties.Delegates

class AddNeedFragment(
    private val needViewModel: NeedViewModel,
    private val need: NeedBody.NeedItem?
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddNeedBinding
    private var requireActivity: FragmentActivity? = null
    private var selectedLocationX = 0.0
    private var selectedLocationY = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddNeedBinding.inflate(inflater, container, false)
        mapFunction()
        fillParameters(need)
        getFormFieldsFromBackend()
        recurrenceListener()
        typeFieldListener()
        submitNeed(need == null)
        return binding.root
    }

    /** It fills the layout's fields corresponding data if it is editNeed
     * It checks whether it is editNeed by checking if need is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(need: NeedBody.NeedItem?) {
        if (need != null) {
            binding.tvAddNeed.text = getString(R.string.edit_need)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spNeedType.setText(need.type)
            binding.spNeedSubType.setText(need.details["subtype"])
            binding.etQuantity.editText?.setText(need.initialQuantity.toString())

            selectedLocationX = need.x
            selectedLocationY = need.y
            coordinateToAddress()
        }
    }

    private fun validateRecurrence(): Boolean{
        val validated = if (binding.swRecurrenceFilter.isChecked){
            validateFields(binding.layRecurrence)
        } else{
            true
        }
        return validated
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

            if (validateFields(binding.laySpecific) && validateRecurrence() && validateFields(binding.layOthers) && validateHardcodedFields()) {

                val othersList = getOthersList()
                var description: String? = ""
                var occurAt: String? = ""
                var recurrenceRate: Int? = null
                var recurrenceDeadline: String? = ""
                var urgency: Int = 1
                for (field in othersList) {
                    Log.i("NEW VALUE", "fieldname: ${field.fieldName}")
                    when (field.fieldName) {
                        "Description" -> description = field.input
                        "Urgency" -> urgency = field.input.toInt()
                        "Occur At" -> occurAt = field.input
                        "Recurrence Rate" -> {
                            if (field.input.isNotEmpty())
                                recurrenceRate = field.input.toInt()
                        }

                        "Recurrence Deadline" -> recurrenceDeadline = field.input
                    }
                }

                if (description == "") description = null
                occurAt = if (occurAt == "") null else dateForBackend(occurAt!!)
                recurrenceDeadline =
                    if (recurrenceDeadline == "") null else dateForBackend(recurrenceDeadline!!)
                Log.i("occurAt", "occurAt: $occurAt")

                val subtypeAsLst = mutableListOf<NeedBody.DetailedFields>()
                val subType = binding.spNeedSubType.text.toString().trim()
                subtypeAsLst.add(NeedBody.DetailedFields("subtype", subType))
                val detailedList = getDetailsList(subtypeAsLst)
                val detailsMap = mutableMapOf<String, String>()
                for (item in detailedList) {
                    detailsMap[item.fieldName] = item.input
                }

                val type2 = binding.spNeedType.text.toString().trim()
                val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
                val coordinateX = selectedLocationX
                val coordinateY = selectedLocationY
                val openAddress = binding.etOpenAddress.text.toString().trim()

                //val newNeed = Need(StringUtil.generateRandomStringID(), creatorName, type, details, date, quantity, coordinateX, coordinateY, 1)
                //needViewModel.insertNeed(need) insert local db
                val needPost = NeedBody.NeedRequestBody(
                    description, quantity, urgency, quantity, type2, detailsMap, openAddress,
                    coordinateX, coordinateY, occurAt, recurrenceRate, recurrenceDeadline
                )
                editOrPostNeed(needPost,isAdd)
                if (binding.swRecurrenceFilter.isChecked){
                    createRecurrence()
                }

            } else {
                if (isAdded)
                    Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun createRecurrence(){

    }

    /**
     * This is for post need into backend
     */
    private fun editOrPostNeed(needPost: NeedBody.NeedRequestBody, isAdd: Boolean ){
        if (isAdd) {
            needViewModel.postNeedRequest(needPost)
        } else {
            val needID = "/" + need!!._id // comes from older need
            needViewModel.postNeedRequest(needPost, needID)
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
    }

    /**
     * if user open recurrence, recurrence relations should be visible
     * these layout items are not visible at first (this code is in xml)
     */
    private fun recurrenceListener(){
        binding.swRecurrenceFilter.setOnClickListener {
            binding.layRecurrence.visibility = if (binding.swRecurrenceFilter.isChecked) View.VISIBLE else View.GONE
        }
    }

    /**
     * It gets the values from layout others and store them in a structured way with OtherFields in a list
     */
    private fun getOthersList(): MutableList<NeedBody.OtherFields> {
        val otherList = mutableListOf<NeedBody.OtherFields>()
        // Iterate through each child view in the layOthers layout
        for (i in 0 until binding.layOthers.childCount) {
            // Check the type of the child view
            when (val childView = binding.layOthers.getChildAt(i)) {
                is TextInputLayout -> {
                    val editText = childView.editText
                    if (editText != null) {
                        val fieldName = editText.hint.toString()
                        val userInput = editText.text.toString().trim()
                        otherList.add(NeedBody.OtherFields(fieldName, userInput))
                        // Use fieldName and userInput as needed
                        Log.i("USER_INPUT", "Others Text Field: $fieldName, Value: $userInput")
                    }
                }

                is MaterialAutoCompleteTextView -> {
                    val fieldName = childView.hint.toString()
                    val userInput = childView.text.toString().trim()
                    otherList.add(NeedBody.OtherFields(fieldName, userInput))
                    // Use fieldName and userInput as needed
                    Log.i("USER_INPUT", "Others Material Field: $fieldName, Value: $userInput")
                }
                // Add additional cases for other view types as needed
            }
        }
        return otherList
    }

    /**
     * It gets the values from layout others and store them in a structured way with OtherFields in a list
     * textInputEditText.inputType = when (field.type){
    "text" ->  InputType.TYPE_CLASS_TEXT
    "number" -> InputType.TYPE_CLASS_NUMBER
    "date" ->  {
    textInputEditText.setOnClickListener {
    showDatePickerDialog(textInputEditText)
    }
    InputType.TYPE_NULL
    }
     */
    private fun getDetailsList(detailsList: MutableList<NeedBody.DetailedFields>): MutableList<NeedBody.DetailedFields> {
        // Iterate through each child view in the layOthers layout
        for (i in 0 until binding.laySpecific.childCount) {
            // Check the type of the child view
            when (val childView = binding.laySpecific.getChildAt(i)) {
                is TextInputLayout -> {
                    val editText = childView.editText
                    if (editText != null) {
                        val fieldName = editText.hint.toString()
                        var userInput = editText.text.toString().trim()
                        if (editText.inputType == InputType.TYPE_NULL) userInput =
                            dateForBackend(userInput)
                        detailsList.add(NeedBody.DetailedFields(fieldName, userInput))
                        // Use fieldName and userInput as needed
                        Log.i("USER_INPUT", "Details Text Field: $fieldName, Value: $userInput")
                    }
                }

                is MaterialAutoCompleteTextView -> {
                    val fieldName = childView.hint.toString()
                    val userInput = childView.text.toString().trim()
                    detailsList.add(NeedBody.DetailedFields(fieldName, userInput))
                    // Use fieldName and userInput as needed
                    Log.i("USER_INPUT", "Details Material Field: $fieldName, Value: $userInput")
                }
                // Add additional cases for other view types as needed
            }
        }
        return detailsList
    }

    private fun typeFieldListener() {
        // Listener for Type field
        val spNeedType = binding.spNeedType
        spNeedType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
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
    }

    /**
     * This function gets the form fields from backend and show them in Form dynamically
     * This form is layOther which is for recurrence relations
     */
    private fun getFormFieldsFromBackend() {
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
                                        if (name == "x" || name == "y" || name == "initialQuantity" || name == "unsuppliedQuantity"
                                            || name == "occur_at" || name == "recurrence_rate" || name == "recurrence_deadline" ||  name == "open_address") {
                                            // Pass
                                        } else{
                                            when (field.type) {
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
                                                        var hint = label
                                                        if (label == "Urgency"){
                                                            hint = getString(R.string.select_1_5)
                                                        }
                                                        textInputLayout.hint = hint
                                                        textInputLayout.isEndIconVisible = true
                                                        textInputLayout.endIconMode =
                                                            TextInputLayout.END_ICON_DROPDOWN_MENU
                                                        textInputLayout.layoutParams =
                                                            LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                            )

                                                        val materialAutoCompleteTextView =
                                                            MaterialAutoCompleteTextView(
                                                                textInputLayout.context
                                                            )
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

                                                else -> {
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
                                                    when (field.type) {
                                                        "text" -> textInputEditText.inputType =
                                                            InputType.TYPE_CLASS_TEXT

                                                        "date" -> {
                                                            textInputEditText.inputType =
                                                                InputType.TYPE_NULL
                                                            textInputEditText.setOnClickListener {
                                                                showDatePickerDialog(
                                                                    textInputEditText
                                                                )
                                                            }
                                                        }
                                                    }
                                                    textInputLayout.addView(textInputEditText)
                                                    othersLayout.addView(textInputLayout)
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
                                    val fields: List<NeedBody.NeedFormFields> =
                                        formFieldsNeedResponse.fields
                                    arrangeDynamicFields(fields)
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


    /**
     * This function arranges the form with respect to dynamic fields from backend
     */
    private fun arrangeDynamicFields(fields: List<NeedBody.NeedFormFields>) {
        val specificLayout = binding.laySpecific
        val spNeedSubType = binding.spNeedSubType
        for (field in fields) {
            val name = field.name
            val label = field.label

            when (field.type) {
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
                        val textInputLayout =
                            TextInputLayout(requireContext(), null, R.attr.customDropDownStyle)
                        textInputLayout.hint = label
                        textInputLayout.isEndIconVisible = true
                        textInputLayout.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                        textInputLayout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        )

                        val materialAutoCompleteTextView =
                            MaterialAutoCompleteTextView(textInputLayout.context)
                        materialAutoCompleteTextView.inputType = InputType.TYPE_CLASS_TEXT

                        textInputLayout.addView(materialAutoCompleteTextView)
                        specificLayout.addView(textInputLayout)


                        // Create Spinner for select type
                        val options = field.options ?: emptyList()
                        val optionsAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            options
                        )
                        materialAutoCompleteTextView.setAdapter(optionsAdapter)
                    }
                }

                else -> {
                    val textInputLayout =
                        TextInputLayout(requireContext(), null, R.attr.customTextInputStyle)
                    textInputLayout.hint = label
                    textInputLayout.isEndIconVisible = true
                    textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    textInputLayout.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                    )
                    val textInputEditText = TextInputEditText(textInputLayout.context)
                    textInputEditText.inputType = when (field.type) {
                        "text" -> InputType.TYPE_CLASS_TEXT
                        "number" -> InputType.TYPE_CLASS_NUMBER
                        "date" -> {
                            textInputEditText.setOnClickListener {
                                showDatePickerDialog(textInputEditText)
                            }
                            InputType.TYPE_NULL
                        }

                        else -> InputType.TYPE_CLASS_TEXT
                    }
                    textInputEditText.onFocusChangeListener =
                        View.OnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                // Handle user input here when focus is lost
                                Log.i(
                                    "ENTERED",
                                    "AA ${textInputEditText.text.toString().trim()}"
                                )
                            }
                        }
                    textInputLayout.addView(textInputEditText)
                    specificLayout.addView(textInputLayout)
                }
            }
        }
    }

    /**
     * It checks validation of hardCoded pieces in the xml
     */
    private fun validateHardcodedFields(): Boolean{
        var returnVal = true // return val
        // if check for open address from layOthers (dynamic list)
        if (binding.etInputCoordinate.text.isNullOrEmpty() && binding.etOpenAddress.text.isNullOrEmpty()){
            binding.etCoordinate.error = "Cannot be empty"
            binding.tvOpenAddress.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.spNeedType.text.isNullOrEmpty()){
            binding.boxNeedType.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.spNeedSubType.text.isNullOrEmpty()){
            binding.boxNeedSubType.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.quantityInput.text.isNullOrEmpty()){
            binding.etQuantity.error = "Cannot be empty"
            returnVal = false
        }
        return returnVal
    }

    private fun validateFields(layout: ViewGroup): Boolean {
        var isValid = true

        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)

            if (view is TextInputLayout) {
                val editText = view.editText
                if (editText != null) {
                    val hint = editText.hint
                    if (hint != "Description" || !hint.contains("Open")  ) {
                        Log.i("LogHint",hint.toString())
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
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                textInputEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun mapFunction(){
        parentFragmentManager.setFragmentResultListener(
            "coordinatesKey",
            viewLifecycleOwner
        ) { _, bundle ->
            selectedLocationX = bundle.getDouble("x_coord")
            selectedLocationY = bundle.getDouble("y_coord")
            coordinateToAddress(
                selectedLocationX,
                selectedLocationY,
                object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
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

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
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
        binding.etCoordinate.setStartIconOnClickListener {
            navigateToMapFragment()
        }
    }

    /**
     * It should open as dialog, else the entered inputs will disappear
     */
    private fun navigateToMapFragment() {
        val mapFragment = ActivityMap()
        mapFragment.isDialog = true // arrange that as a dialog instead of fragment
        mapFragment.coordinatesSelectedListener = this@AddNeedFragment
        mapFragment.show(parentFragmentManager, "mapDialog")
    }


    private fun coordinateToAddress(){
        coordinateToAddress(
            selectedLocationX,
            selectedLocationY,
            object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
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

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
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


private fun coordinateToAddress(x: Double, y: Double, callback: okhttp3.Callback) {
    val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    client.newCall(request).enqueue(callback)
}
