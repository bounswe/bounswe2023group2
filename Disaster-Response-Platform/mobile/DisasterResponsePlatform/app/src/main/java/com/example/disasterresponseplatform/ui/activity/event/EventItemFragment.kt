package com.example.disasterresponseplatform.ui.activity.event

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentEventItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.report.ReportBottomSheetFragment
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import okhttp3.OkHttpClient
import okhttp3.Request

class EventItemFragment(private val eventViewModel: EventViewModel, private val event: EventBody.EventRequestBody) : Fragment() {

    private lateinit var binding: FragmentEventItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorEvent)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorEvent)

        binding = FragmentEventItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        fillTexts(event)
        arrangeButtons()
    }

    private fun fillTexts(event: EventBody.EventRequestBody){
        val creatorName = event.created_by_user
        binding.tvCreator.text = creatorName
        binding.tvType.text = event.event_type
        binding.tvDescription.text = event.short_description
        binding.tvCreationTime.text = event.created_time
        if (event.last_confirmed_time == null){
            binding.iconUpdate.isVisible = false
            binding.tvLastUpdatedTimeTitle.isVisible = false
            binding.tvLastUpdatedTime.isVisible = false
        } else {
            binding.tvLastUpdatedTime.text = event.last_confirmed_time
        }
        if (event.note == null){
            binding.iconDetails.isVisible = false
            binding.tvNote.isVisible = false
            binding.tvDetailsTitle.isVisible = false
        } else{
            binding.tvNote.text = event.note
        }
        userViewModel.getUserRole(creatorName)
        userViewModel.getLiveDataUserRole().observe(requireActivity!!){
            val userRole = if (it == "null") "AUTHENTICATED" else it
            binding.tvUserRole.text = userRole
        }
        binding.tvAddress.text = "%.3f %.3f".format(event.x, event.y)
        coordinateToAddress(event.x, event.y, object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("Network", "Error: ${e.message}")
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.i("Network", "Response: $responseBody")
                if (responseBody != null) {
                    var address = responseBody.subSequence(
                        responseBody.indexOf("display_name") + 15,
                        responseBody.length
                    )
                    address = address.subSequence(0, address.indexOf("\""))
                    requireActivity?.runOnUiThread {
                        binding.tvAddress.text = address
                    }
                }
            }
        })
    }

    /**
     * It arranges buttons, if user is authenticated and the creator of the item user can edit and delete
     * else these buttons are gone
     */
    private fun arrangeButtons(){
        val token = DiskStorageManager.getKeyValue("token")
        val username = DiskStorageManager.getKeyValue("username").toString() // only creators can edit it

        if (!DiskStorageManager.checkToken()) {
            binding.iconReliability.visibility = View.GONE
            binding.tvReliability.visibility = View.GONE
            binding.btnUpvote.visibility = View.GONE
            binding.btnDownvote.visibility = View.GONE
            binding.btnReport.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
        } else {
            binding.iconReliability.visibility = View.VISIBLE
            binding.tvReliability.visibility = View.VISIBLE
            binding.btnUpvote.visibility = View.VISIBLE
            binding.btnDownvote.visibility = View.VISIBLE
            binding.btnReport.visibility = View.VISIBLE

            if (username == event.created_by_user) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener {
                    editEvent()
                }
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    deleteEvent()
                }
            } else {
                binding.btnEdit.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }
        }

        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
        binding.btnSeeProfile.setOnClickListener {
            addFragment(ProfileFragment(event.created_by_user),"ProfileFragment")
        }

        // Arrange vote buttons and set on click listener
        arrangeVoteButtons()
        binding.btnUpvote.setOnClickListener {
            voteEvent(token, "up")
        }
        binding.btnDownvote.setOnClickListener {
            voteEvent(token,"down")
        }

        binding.btnReport.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun arrangeVoteButtons() {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if (DiskStorageManager.checkToken()) {
            voteViewModel.checkvote("events", event._id)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!) {
                when (it) {
                    "upvote" -> {
                        btnUpvote.isChecked = true
                        btnDownvote.isChecked = false
                    }

                    "downvote" -> {
                        btnUpvote.isChecked = false
                        btnDownvote.isChecked = true
                    }

                    "none" -> {
                        btnUpvote.isChecked = false
                        btnDownvote.isChecked = false
                    }
                }
            }
        } else {
            btnUpvote.isChecked = false
            btnDownvote.isChecked = false
        }
    }

    /**
     * It upvotes/downvotes/unvotes the event, arranges the buttons and shows toast accordingly
     */
    @SuppressLint("SetTextI18n")
    private fun voteEvent(token: String?, type: String) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if ((!token.isNullOrEmpty()) && (isAdded)){

            // if already upvoted, UNvote and arrange button
            if (type == "up" && btnUpvote.isChecked) {
                showToast("Your upvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("events", event._id))
                btnUpvote.isChecked = false
            }
            // if already downvoted, UNvote and arrange button
            else if (type == "down" && btnDownvote.isChecked) {
                showToast("Your downvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("events", event._id))
                btnDownvote.isChecked = false
            }
            // if not upvoted, upvote and arrange buttons
            else if (type == "up" && !btnUpvote.isChecked) {
                showToast("Your upvote has been saved.")
                voteViewModel.upvote(VoteBody.VoteRequestBody("events",event._id))
                btnUpvote.isChecked = true
                btnDownvote.isChecked = false
            }
            // if not downvoted, downvote and arrange buttons
            else if (type == "down" && !btnDownvote.isChecked) {
                showToast("Your downvote has been saved.")
                voteViewModel.downvote(VoteBody.VoteRequestBody("events",event._id))
                btnUpvote.isChecked = false
                btnDownvote.isChecked = true
            }

        } else if ((token.isNullOrEmpty()) && (isAdded)) {
            showToast(getString(R.string.pr_login_required))
            btnUpvote.isChecked = false
            btnDownvote.isChecked = false
        }
    }

    private fun editEvent(){
        val addEventFragment = AddEventFragment(eventViewModel,event)
        addFragment(addEventFragment,"AddEventFragment")
    }

    /** This function is called when user wants to delete event
     * If the creator of event and users match, user can delete event
     */
    private fun deleteEvent(){
        eventViewModel.deleteEvent(event._id)
        eventViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    showToast("Successfully Deleted")
                } else{
                    showToast("Cannot Deleted Check Logs")
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                if (isAdded) // to ensure it attached a parentFragmentManager
                    parentFragmentManager.popBackStack("EventItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }, 200)
        }
    }

    private fun navigateToMapFragment() {
        val mapFragment = ActivityMap()
        addFragment(mapFragment,"ActivityMap")
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = ReportBottomSheetFragment(event._id, "events")
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}