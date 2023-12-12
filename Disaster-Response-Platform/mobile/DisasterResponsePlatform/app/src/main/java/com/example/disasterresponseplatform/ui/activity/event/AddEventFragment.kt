package com.example.disasterresponseplatform.ui.activity.event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.databinding.FragmentAddEventBinding
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.activity.util.map.OnCoordinatesSelectedListener
import com.example.disasterresponseplatform.utils.DateUtil
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Calendar
import kotlin.properties.Delegates


class AddEventFragment(private val eventViewModel: EventViewModel, private val event: EventBody.EventRequestBody?) : Fragment(),
    OnCoordinatesSelectedListener {

    private lateinit var binding: FragmentAddEventBinding
    private var requireActivity: FragmentActivity? = null
    private var selectedLocationX by Delegates.notNull<Double>()
    private var selectedLocationY by Delegates.notNull<Double>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        initView()
        mapFunction()
        fillParameters(event)
        submitEvent(event == null)
        return binding.root
    }
    override fun onCoordinatesSelected(x: Double, y: Double) {}

    @SuppressLint("SetTextI18n")
    private fun fillParameters(event: EventBody.EventRequestBody?) {
        if (event != null) {
            binding.tvAddEvent.text = getString(R.string.edit_event)
            binding.btnSubmit.text = getString(R.string.save_changes)
            binding.spEventType.setText(event.event_type)
            binding.etDate.editText?.setText(event.event_time) // TODO seperate it as date and time with util func
            if (event.max_distance_x != null) binding.tvCoverageX.editText?.setText(event.max_distance_x.toInt())
            if (event.max_distance_y != null) binding.tvCoverageY.editText?.setText(event.max_distance_y.toInt())
            binding.tvShortDescription.editText?.setText(event.short_description)
            binding.tvNotes.editText?.setText(event.note)

            selectedLocationX = event.center_location_x
            selectedLocationY = event.center_location_y
            coordinateToAddress()
        }
    }

    @SuppressLint("ResourceType")
    private fun initView(){
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        val eventTypesList: List<String> = resources.getStringArray(R.array.event_types).toList()
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            eventTypesList
        )

// Set the adapter to the AutoCompleteTextView
        binding.spEventType.setAdapter(adapter)
        binding.etTimeInput.setOnClickListener { showTimePicker() }
        binding.etDateInput.setOnClickListener { showDatePicker() }
    }

    private fun submitEvent(isAdd: Boolean) {

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isEnabled) { // Prevent multiple clicks
                return@setOnClickListener
            }
            binding.btnSubmit.isEnabled = false
            val layAddEvent = binding.layAddEvent
            if (validateFields(layAddEvent)) {

                var creationEventDate: String? = null
                if (binding.etDateInput.text != null && binding.etTimeInput.text != null){
                    val eventDate = DateUtil.dateForBackend(binding.etDateInput.text.toString())
                    val eventTime = binding.etTimeInput.text
                    creationEventDate = "$eventDate $eventTime"
                    Log.i("creationEventDate",creationEventDate)
                }

                val additionalNote = binding.etNotes.text.toString().trim()
                val shortNote = binding.etShortDescription.text.toString().trim()
                val maxDistanceX = if (binding.etCoverageX.text != null) binding.etCoverageX.text.toString().trim().toDouble() else null
                val maxDistanceY = if (binding.etCoverageY.text != null) binding.etCoverageY.text.toString().trim().toDouble() else null
                val createdTime = "${DateUtil.getDate("yyyy-MM-dd")} ${DateUtil.getTime("HH:mm:ss")}"
                val postedEvent = EventBody.EventPostBody(
                    event_type = binding.spEventType.text.toString().trim(),
                    event_time =  creationEventDate,
                    is_active = true,
                    center_location_x = selectedLocationX,
                    center_location_y = selectedLocationY,
                    max_distance_x = maxDistanceX,
                    max_distance_y = maxDistanceY,
                    created_time = createdTime ,
                    short_description = shortNote,
                    note = additionalNote)

                if (isAdd) {
                    eventViewModel.postEvent(postedEvent)
                } else {
                    val eventID = "/" + event!!._id // comes from older need
                    eventViewModel.postEvent(postedEvent, eventID)
                }
                eventViewModel.getLiveDataEventID().observe(requireActivity!!) {
                    if (it != "-1") { // in error cases it returns this
                        if (isAdded) { // to ensure it attached a context
                            if (isAdd)
                                Toast.makeText(requireContext(), "Created Event ID: $it", Toast.LENGTH_LONG).show()
                            else
                                Toast.makeText(requireContext(), "UPDATED", Toast.LENGTH_SHORT).show()
                        }
                        Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                            if (isAdded) // to ensure it attached a parentFragmentManager
                                parentFragmentManager.popBackStack("AddEventFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            if (!isAdd)
                                parentFragmentManager.popBackStack("EventItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            // Re-enable the button after the background operation completes
                            binding.btnSubmit.isEnabled = true
                        }, 200)
                    } else {
                        if (isAdded)
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

    /**
     * Users must enter event type, location and short description
     */
    private fun validateFields(layout: ViewGroup): Boolean {
        var isValid = true

        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)

            if (view is TextInputLayout) {
                val editText = view.editText
                if (editText != null) {
                    val hint = editText.hint
                    if (hint == requireActivity!!.getString(R.string.field_location) || hint == requireActivity!!.getString(R.string.field_event_type)
                        || hint == requireActivity!!.getString(R.string.short_note)) {
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

    private fun showDatePicker() {
        // Get the current date
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity!!,
            OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                binding.etDate.editText?.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        // Get the current time
        val calendar: Calendar = Calendar.getInstance()
        val hourOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        // Create a TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireActivity!!,
            OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = "$hourOfDay:$minute"
                binding.etTime.editText?.setText(selectedTime)
            },
            hourOfDay,
            minute,
            true // 24-hour format
        )

        // Show the TimePickerDialog
        timePickerDialog.show()
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
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
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
        binding.etCoordinate.setEndIconOnClickListener {
            navigateToMapFragment()
        }
    }

    private fun navigateToMapFragment() {
        val mapFragment = ActivityMap()
        mapFragment.coordinatesSelectedListener = this@AddEventFragment
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mapFragment)
        transaction.addToBackStack(null)
        transaction.commit()
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

    private fun coordinateToAddress(x: Double, y: Double, callback: okhttp3.Callback) {
        val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

}