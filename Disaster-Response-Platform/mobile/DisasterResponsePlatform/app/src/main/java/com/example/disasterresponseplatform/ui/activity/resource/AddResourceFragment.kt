package com.example.disasterresponseplatform.ui.activity.resource

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
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.RecurrenceModel
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.databinding.FragmentAddResourceBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.RecurrenceViewModel
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.example.disasterresponseplatform.utils.Annotation
import com.example.disasterresponseplatform.utils.DateUtil
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.IOException
import java.util.Calendar
import kotlin.properties.Delegates

class AddResourceFragment(
    private val resourceViewModel: ResourceViewModel,
    private val resource: ResourceBody.ResourceItem?
) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddResourceBinding
    private var requireActivity: FragmentActivity? = null
    private var selectedLocationX = 0.0
    private var selectedLocationY = 0.0
    private val mapFragment = ActivityMap()
    private var annotation = Annotation()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddResourceBinding.inflate(inflater, container, false)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        trackUserPickLocation()
        fillParameters(resource)
        getFormFieldsFromBackend()
        initRecurrenceView()
        recurrenceListener()
        typeFieldListener()
        submitResource(resource == null)
        return binding.root
    }

    /** It fills the layout's fields corresponding data if it is editResource
     * It checks whether it is editResource by checking if resource is null, if it is not null then it should be edit form
     */
    @SuppressLint("SetTextI18n")
    private fun fillParameters(resource: ResourceBody.ResourceItem?) {
        if (resource != null) {
            binding.tvAddResource.text = getString(R.string.edit_resource)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spResourceType.setText(resource.type.toString())
            binding.spResourceSubType.setText(resource.details["subtype"])
            binding.etQuantity.editText?.setText(resource.currentQuantity.toString())

            selectedLocationX = resource.x
            selectedLocationY = resource.y
            coordinateToAddress()
        }
    }

    /**
     * This function arranges submit operation, if isAdd is true it should be POST to backend, else it should be PUT.
     */
    private fun submitResource(isAdd: Boolean) {
        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false

            if (validation()) {
                val resourcePost = generatePostResource()
                editOrPostResource(isAdd,resourcePost)
            } else {
                if (isAdded)
                    Toast.makeText(context, "Check the Fields", Toast.LENGTH_LONG).show()
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun generatePostResource():ResourceBody.ResourceRequestBody{
        val othersList = getOthersList()
        var description: String? = ""
        var condition: String = "new"
        for (field in othersList) {
            Log.i("NEW VALUE", "fieldname: ${field.fieldName}")
            when (field.fieldName) {
                "Description" -> description = field.input
                "Condition" -> condition = field.input
            }
        }
        if (description == "") description = null

        var startAt: String? = null
        var recurrenceRate: Int? = null
        var endAt: String? = null
        if (binding.swRecurrenceFilter.isChecked){
            recurrenceRate = binding.spRecurrenceRate.text.toString().trim().toInt() // 1,2,3 ?
            startAt = DateUtil.dateForBackend(binding.etStartDateInput.text.toString())
            endAt = DateUtil.dateForBackend(binding.etEndDateInput.text.toString())
        }

        val subtypeAsLst = mutableListOf<ResourceBody.DetailedFields>()
        val subType = binding.spResourceSubType.text.toString().trim()
        subtypeAsLst.add(ResourceBody.DetailedFields("subtype", subType))
        val detailedList = getDetailsList(subtypeAsLst)
        val detailsMap = mutableMapOf<String, String>()
        for (item in detailedList) {
            detailsMap[item.fieldName] = item.input
        }

        val type2 = binding.spResourceType.text.toString().trim()
        val quantity = binding.etQuantity.editText?.text.toString().trim().toInt()
        val coordinateX = selectedLocationX
        val coordinateY = selectedLocationY
        val openAddress = binding.etOpenAddress.text.toString().trim()

        if (description != null) {
            annotation.publishAnnotation(DiskStorageManager.getKeyValue("username")!! + "-resource-" + subType, description)
        }

        //val newResource = Resource(StringUtil.generateRandomStringID(), creatorName, type, details, date, quantity, coordinateX, coordinateY, 1)
        //resourceViewModel.insertResource(resource) insert local db
        return ResourceBody.ResourceRequestBody(
            description, quantity, quantity, type2, detailsMap,openAddress,
            coordinateX, coordinateY, startAt, recurrenceRate, endAt
        )
    }

    //explained in AddNeedFragment
    private fun editOrPostResource(isAdd: Boolean, resourcePost: ResourceBody.ResourceRequestBody){
        if (isAdd) {
            resourceViewModel.postResourceRequest(resourcePost)
        } else {
            val resourceID = "/" + resource!!._id // comes from older resource
            resourceViewModel.postResourceRequest(resourcePost, resourceID)
        }
        resourceViewModel.getLiveDataResourceID().observe(requireActivity!!) {  resourceID->
            if (resourceID != "-1") { // in error cases it returns this
                if (isAdded) { // to ensure it attached a context
                    if (isAdd){
                        if (binding.swRecurrenceFilter.isChecked){
                            Toast.makeText(requireContext(), "Created Resource ID: $resourceID", Toast.LENGTH_LONG).show()
                            val recurrenceBody = generatePostRecurrence()
                            Log.i("Recurrence","Resource Called createRecurrence")
                            createRecurrence(recurrenceBody, RecurrenceViewModel(),resourceID)
                        }
                    }
                    else
                        Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT).show()
                }

                Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                    if (isAdded) // to ensure it attached a parentFragmentManager
                        parentFragmentManager.popBackStack("AddResourceFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    if (!isAdd)
                        parentFragmentManager.popBackStack("ResourceItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    // Re-enable the button after the background operation completes
                    binding.btnSubmit.isEnabled = true
                }, 200)
            } else {
                if (isAdded)
                    Toast.makeText(requireContext(), "Error Check Logs", Toast.LENGTH_SHORT).show()
                binding.btnSubmit.isEnabled = true
            }
        }
    }

    private fun generatePostRecurrence(): RecurrenceModel.PostRecurrenceBody{
        val activity: String = "Resource" // Need, Resource, Event, Emergency, Action
        val recurrenceRate = binding.spRecurrenceRate.text.toString().trim().toInt() // 1,2,3 ?
        val recurrenceUnit = binding.spRecurrenceUnit.text.toString().trim() // day, week, month
        val startAt = DateUtil.dateForBackend(binding.etStartDateInput.text.toString())
        val endAt = DateUtil.dateForBackend(binding.etEndDateInput.text.toString())
        val title = "Resource $startAt to $endAt"
        val description: String = "Resource $startAt to $endAt"

        return RecurrenceModel.PostRecurrenceBody(
            title = title,
            description = description,
            activity = activity,
            occurance_rate = recurrenceRate,
            occurance_unit = recurrenceUnit,
            duration = null,
            start_at = startAt,
            end_at = endAt
        )
    }

    private fun createRecurrence(recurrenceBody: RecurrenceModel.PostRecurrenceBody, recurrenceViewModel: RecurrenceViewModel, resourceID: String){
        recurrenceViewModel.postRecurrence(recurrenceBody)
        recurrenceViewModel.getCreatedRecurrenceID().observe(requireActivity!!){ createdRecurrenceID ->
            if (createdRecurrenceID != "-1"){ // created successfully
                Log.i("Recurrence","Resource Called attachRecurrence")
                attachRecurrence(recurrenceViewModel,createdRecurrenceID,resourceID)
            }else{
                Log.i("Recurrence","Resource createRecurrence Error")
            }

        }
    }

    private fun attachRecurrence(recurrenceViewModel: RecurrenceViewModel, recurrenceID: String, resourceID: String){
        val activityType = "resource"
        val attachBody = RecurrenceModel.AttachActivityBody(recurrenceID,resourceID,activityType)
        recurrenceViewModel.attachRecurrence(attachBody)
        recurrenceViewModel.getAttachedRecurrenceID().observe(requireActivity!!){attachedID ->
            if (attachedID!="-1"){
                Log.i("Recurrence","Called startRecurrence")
                startRecurrence(attachedID,recurrenceViewModel)
            }else{
                Log.i("Recurrence","Error attachRecurrence")
            }
        }
    }

    /**
     * This is for start recurrence after its attachment is arranged
     */
    private fun startRecurrence(attachedID: String, recurrenceViewModel: RecurrenceViewModel){
        recurrenceViewModel.startRecurrence(attachedID)
        recurrenceViewModel.getIsRecurrenceStarted().observe(requireActivity!!){started->
            if (started){
                Log.i("Recurrence","startRecurrence Success")
            }else{
                Log.i("Recurrence","startRecurrence Error")
            }
        }
    }

    private fun recurrenceListener(){
        binding.swRecurrenceFilter.setOnClickListener {
            binding.layRecurrence.visibility = if (binding.swRecurrenceFilter.isChecked) View.VISIBLE else View.GONE
        }
    }

    private fun initRecurrenceView(){

        val recurrenceUnitList: List<String> = resources.getStringArray(R.array.recurrence_unit).toList()
        val unitAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            recurrenceUnitList
        )
        val recurrenceRateList: List<Int> = listOf(1,2,3,2880)
        val rateAdapter = ArrayAdapter<Int>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            recurrenceRateList
        )

        binding.spRecurrenceUnit.setAdapter(unitAdapter)
        binding.spRecurrenceRate.setAdapter(rateAdapter)
        binding.etStartDateInput.setOnClickListener { showDatePicker(true) }
        binding.etEndDateInput.setOnClickListener { showDatePicker(false) }
    }

    private fun showDatePicker(isStart: Boolean) {
        // Get the current date
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity!!,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                if (isStart)
                    binding.etStartDate.editText?.setText(selectedDate)
                else
                    binding.etEndDate.editText?.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    /**
     * It gets the values from layout others and store them in a structured way with OtherFields in a list
     */
    private fun getOthersList(): MutableList<ResourceBody.OtherFields> {
        val otherList = mutableListOf<ResourceBody.OtherFields>()
        // Iterate through each child view in the layOthers layout
        for (i in 0 until binding.layOthers.childCount) {
            // Check the type of the child view
            when (val childView = binding.layOthers.getChildAt(i)) {
                is TextInputLayout -> {
                    val editText = childView.editText
                    if (editText != null) {
                        val fieldName = editText.hint.toString()
                        val userInput = editText.text.toString().trim()
                        otherList.add(ResourceBody.OtherFields(fieldName, userInput))
                        // Use fieldName and userInput as needed
                        Log.i("USER_INPUT", "Others Text Field: $fieldName, Value: $userInput")
                    }
                }

                is MaterialAutoCompleteTextView -> {
                    val fieldName = childView.hint.toString()
                    val userInput = childView.text.toString().trim()
                    otherList.add(ResourceBody.OtherFields(fieldName, userInput))
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
    private fun getDetailsList(detailsList: MutableList<ResourceBody.DetailedFields>): MutableList<ResourceBody.DetailedFields> {
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
                            DateUtil.dateForBackend(userInput)
                        detailsList.add(ResourceBody.DetailedFields(fieldName, userInput))
                        // Use fieldName and userInput as needed
                        Log.i("USER_INPUT", "Details Text Field: $fieldName, Value: $userInput")
                    }
                }

                is MaterialAutoCompleteTextView -> {
                    val fieldName = childView.hint.toString()
                    val userInput = childView.text.toString().trim()
                    detailsList.add(ResourceBody.DetailedFields(fieldName, userInput))
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
        val spResourceType = binding.spResourceType
        spResourceType.addTextChangedListener(object : TextWatcher {
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
                val selectedType = spResourceType.text.toString()
                typeChanged(selectedType)
            }
        })
    }

    private fun trackUserPickLocation(){
        mapFragment.getLocationChosen().observe(requireActivity!!){chosen ->
            if (chosen){
                Log.i("LocationMAP","IS CHOSEN")
                parentFragmentManager.setFragmentResultListener(
                    "coordinatesKey",
                    viewLifecycleOwner
                ) { _, bundle ->
                    selectedLocationX = bundle.getDouble("x_coord")
                    selectedLocationY = bundle.getDouble("y_coord")
                    coordinateToAddress(
                        selectedLocationX,
                        selectedLocationY,
                        object : Callback {
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
                // to ensure it's not continuously listening the port after the location is received
                parentFragmentManager.clearFragmentResultListener("coordinatesKey")
            }
        }
        binding.etCoordinate.setStartIconOnClickListener {
            navigateToMapFragment()
        }
    }

    private fun navigateToMapFragment() {
        mapFragment.isDialog = true // arrange that as a dialog instead of fragment
        mapFragment.coordinatesSelectedListener = this@AddResourceFragment
        mapFragment.show(parentFragmentManager, "mapDialog")
        //val transaction = parentFragmentManager.beginTransaction()
        //transaction.replace(R.id.container, mapFragment)
        //transaction.addToBackStack(null)
        //transaction.commit()
    }

    /**
     * This function gets the form fields from backend and show them in Form dynamically
     */
    private fun getFormFieldsFromBackend() {
        val networkManager = NetworkManager()
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )

        networkManager.makeRequest(
            endpoint = Endpoint.FORM_FIELDS_RESOURCE,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val formFieldsResourceResponse = gson.fromJson(
                                    rawJson,
                                    ResourceBody.ResourceFormFieldsResponse::class.java
                                )
                                if (formFieldsResourceResponse != null) {
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsResourceResponse: $formFieldsResourceResponse"
                                    )

                                    val othersLayout = binding.layOthers
                                    val fields: List<ResourceBody.ResourceFormFields> =
                                        formFieldsResourceResponse.fields

                                    for (field in fields) {
                                        val name = field.name
                                        val label = field.label
                                        if (name == "x" || name == "y" || name == "initialQuantity" || name == "unsuppliedQuantity"
                                            || name == "occur_at" || name == "recurrence_rate" || name == "recurrence_deadline" ||  name == "open_address") {
                                            // Pass
                                        }else{
                                            when (field.type) {
                                                "number" -> {
                                                    val textInputLayout = TextInputLayout(
                                                        requireContext(),
                                                        null,
                                                        R.attr.customTextInputStyle
                                                    )
                                                    textInputLayout.hint = label
                                                    textInputLayout.isEndIconVisible = true
                                                    textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                                                    textInputLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,)

                                                    val textInputEditText = TextInputEditText(textInputLayout.context)
                                                    textInputEditText.inputType = InputType.TYPE_CLASS_NUMBER
                                                    textInputLayout.addView(textInputEditText)
                                                    othersLayout.addView(textInputLayout)
                                                }

                                                "select" -> {
                                                    if (name == "type") {
                                                        // Create Spinner for select type
                                                        val options = field.options ?: emptyList()
                                                        val optionsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
                                                        val spResourceType = binding.spResourceType
                                                        spResourceType.setAdapter(optionsAdapter)
                                                    } else {
                                                        val textInputLayout = TextInputLayout(requireContext(), null, R.attr.customDropDownStyle)
                                                        textInputLayout.hint = label
                                                        textInputLayout.isEndIconVisible = true
                                                        textInputLayout.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                                                        textInputLayout.layoutParams =
                                                            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,)

                                                        val materialAutoCompleteTextView =
                                                            MaterialAutoCompleteTextView(textInputLayout.context)
                                                        materialAutoCompleteTextView.inputType = InputType.TYPE_CLASS_TEXT

                                                        textInputLayout.addView(materialAutoCompleteTextView)
                                                        othersLayout.addView(textInputLayout)

                                                        // Create Spinner for select type
                                                        val options = field.options ?: emptyList()
                                                        val optionsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, options)
                                                        materialAutoCompleteTextView.setAdapter(optionsAdapter)
                                                    }
                                                }

                                                else -> {
                                                    val textInputLayout = TextInputLayout(requireContext(), null, R.attr.customTextInputStyle)
                                                    textInputLayout.hint = label
                                                    textInputLayout.isEndIconVisible = true
                                                    textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                                                    textInputLayout.layoutParams =
                                                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,)
                                                    val textInputEditText = TextInputEditText(textInputLayout.context)
                                                    when (field.type) {
                                                        "text" -> textInputEditText.inputType =
                                                            InputType.TYPE_CLASS_TEXT

                                                        "date" -> {
                                                            textInputEditText.inputType = InputType.TYPE_NULL
                                                            textInputEditText.setOnClickListener {
                                                                showDatePickerDialog(textInputEditText)
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
                            } catch (e: java.io.IOException) {
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
        val spResourceSubType = binding.spResourceSubType
        spResourceSubType.text.clear()
        spResourceSubType.setAdapter(null)

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
            callback = object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val formFieldsResourceResponse = gson.fromJson(
                                    rawJson,
                                    ResourceBody.ResourceFormFieldsResponse::class.java
                                )
                                if (formFieldsResourceResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "formFieldsResourceResponse: $formFieldsResourceResponse"
                                    )
                                    val fields: List<ResourceBody.ResourceFormFields> =
                                        formFieldsResourceResponse.fields
                                    arrangeDynamicFields(fields)
                                }
                            } catch (e: java.io.IOException) {
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
    private fun arrangeDynamicFields(fields: List<ResourceBody.ResourceFormFields>) {
        val specificLayout = binding.laySpecific
        val spResourceSubType = binding.spResourceSubType
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
                        spResourceSubType.setAdapter(optionsAdapter)

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

    private fun validation(): Boolean{
        return (validateFields(binding.laySpecific) &&
                validateRecurrence() &&
                validateFields(binding.layOthers) &&
                validateHardcodedFields())
    }

    private fun validateRecurrence(): Boolean{
        val validated = if (binding.swRecurrenceFilter.isChecked){
            validateFields(binding.layRecurrence)
        } else{
            true
        }
        return validated
    }

    private fun validateHardcodedFields(): Boolean{
        var returnVal = true // return val
        // if check for open address from layOthers (dynamic list)
        if (binding.etInputCoordinate.text.isNullOrEmpty() && binding.etOpenAddress.text.isNullOrEmpty()){
            binding.etCoordinate.error = "Cannot be empty"
            binding.tvOpenAddress.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.spResourceType.text.isNullOrEmpty()){
            binding.boxResourceType.error = "Cannot be empty"
            returnVal = false
        }
        if (binding.spResourceSubType.text.isNullOrEmpty()){
            binding.boxResourceSubType.error = "Cannot be empty"
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
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                textInputEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
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

    private fun coordinateToAddress(){
        coordinateToAddress(
            selectedLocationX,
            selectedLocationY,
            object : Callback {
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
}


private fun coordinateToAddress(x: Double, y: Double, callback: okhttp3.Callback) {
    val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y&api_key=658a6bb850a62680253220cju871eba"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    client.newCall(request).enqueue(callback)
}
