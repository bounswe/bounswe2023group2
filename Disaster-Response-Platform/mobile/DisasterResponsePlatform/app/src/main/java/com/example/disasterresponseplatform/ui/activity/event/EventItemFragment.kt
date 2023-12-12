package com.example.disasterresponseplatform.ui.activity.event

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.disasterresponseplatform.ui.activity.VoteViewModel
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
        binding.tvType.text = event.eventType
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
        binding.tvUpvoteCount.text = event.upvote.toString()
        binding.tvDownVoteCount.text = event.downvote.toString()
        userViewModel.getUserRole(creatorName)
        userViewModel.getLiveDataUserRole().observe(requireActivity!!){
            val userRole = if (it == "null") "AUTHENTICATED" else it
            binding.tvUserRole.text = userRole
        }
        binding.tvAddress.text = "%.3f %.3f".format(event.center_location_x, event.center_location_y)
        coordinateToAddress(event.center_location_x, event.center_location_y, object : okhttp3.Callback {
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
        if (!token.isNullOrEmpty() and (username == event.created_by_user)) {
            binding.btnEdit.setOnClickListener {
                editEvent()
            }
            binding.btnDelete.setOnClickListener {
                deleteEvent()
            }
        } else{
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }
        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
        binding.btnSeeProfile.setOnClickListener {
            addFragment(ProfileFragment(event.created_by_user),"ProfileFragment")
        }
        binding.btnUpvote.setOnClickListener {
            upvoteNeed(token)
        }
        binding.btnDownvote.setOnClickListener {
            downvoteNeed(token)
        }
    }

    private var voted = false // if user change his/her some arrangements will happen with this parameter
    /**
     * It upvotes the need, increment upvote count, make upvote button not clickable and shows toast upvote successfully message
     * If user already upvotes that need it shows toast you already upvoted message
     */
    @SuppressLint("SetTextI18n")
    private fun upvoteNeed(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("needs",event._id)
            voteViewModel.upvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Upvote it!", Toast.LENGTH_SHORT).show()
                }
                else if (it == "upvote"){
                    // if users vote for downvote before (if vote for upvote he can't click again because its not clickable)
                    if (voted){
                        binding.btnDownvote.isClickable = true
                        binding.tvDownVoteCount.text = event.downvote.toString()
                    }
                    voted = true
                    binding.btnUpvote.isClickable = false
                    binding.tvUpvoteCount.text = (event.upvote + 1).toString()

                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),"You need to log in!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * It downvotes the need, increase downvote count, make downvote button not clickable and shows toast downvote successfully message
     * If user already downvote that need it shows toast you already downvote message
     */
    @SuppressLint("SetTextI18n")
    private fun downvoteNeed(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("needs",event._id)
            voteViewModel.downvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Downvote it!", Toast.LENGTH_SHORT).show()
                }
                else if (it == "downvote"){
                    if (voted){
                        binding.btnUpvote.isClickable = true
                        binding.tvUpvoteCount.text = event.upvote.toString()
                    }
                    binding.btnDownvote.isClickable = false
                    binding.tvDownVoteCount.text = (event.downvote + 1).toString()
                    voted = true
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),"You need to log in!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editEvent(){
        val addEventFragment = AddEventFragment(eventViewModel,event)
        addFragment(addEventFragment,"AddEventFragment")
    }

    /** This function is called when user wants to delete need
     * If the creator of need and users match, user can delete need
     */
    private fun deleteEvent(){
        eventViewModel.deleteEvent(event._id)
        eventViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(context, "Cannot Deleted Check Logs", Toast.LENGTH_SHORT).show()
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