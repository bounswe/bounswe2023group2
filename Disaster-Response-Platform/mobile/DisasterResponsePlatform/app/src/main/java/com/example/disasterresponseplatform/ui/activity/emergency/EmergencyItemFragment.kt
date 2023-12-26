package com.example.disasterresponseplatform.ui.activity.emergency

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentEmergencyItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import okhttp3.OkHttpClient
import okhttp3.Request

class EmergencyItemFragment(private val emergencyViewModel: EmergencyViewModel, private val emergency: EmergencyBody.EmergencyItem) : Fragment() {

    private lateinit var binding: FragmentEmergencyItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorEmergency)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorEmergency)

        binding = FragmentEmergencyItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        fillTexts(emergency)
        arrangeButtons()
    }

    private fun fillTexts(emergency: EmergencyBody.EmergencyItem){
        val creatorUsername = emergency.created_by

        // Check whether it is created by user or guest
        if (!creatorUsername.isNullOrEmpty()) {
            binding.tvCreator.text = creatorUsername
            userViewModel.getUserRole(creatorUsername)
            userViewModel.getLiveDataUserRole().observe(requireActivity!!){
                val userRole = if (it == "null") "AUTHENTICATED" else it
                binding.tvUserRole.text = userRole
            }
        } else {
            // Hide Creation time
            binding.iconCreationTime.visibility = View.GONE
            binding.tvCreationTimeTitle.visibility = View.GONE
            binding.tvCreationTime.visibility = View.GONE
            // Hide Update time
            binding.iconUpdate.visibility = View.GONE
            binding.tvLastUpdatedTimeTitle.visibility = View.GONE
            binding.tvLastUpdatedTime.visibility = View.GONE
            //Hide Creator
            binding.iconCreator.visibility = View.GONE
            binding.tvUsernameTitle.visibility = View.GONE
            binding.tvCreator.visibility = View.GONE
            //Hide UserRole
            binding.iconUserRole.visibility = View.GONE
            binding.tvUserRoleTitle.visibility = View.GONE
            binding.tvUserRole.visibility = View.GONE
        }
        binding.tvContactName.text = emergency.contact_name
        binding.tvContactNumber.text = emergency.contact_number
        binding.tvType.text = emergency.type
        binding.tvDescription.text = emergency.description
        binding.tvCreationTime.text = emergency.created_at
        binding.tvLastUpdatedTime.text = emergency.last_updated_at
        binding.tvAddress.text = emergency.location


//        !!--- Emergency is currently running locally ---!!
//
//        binding.tvAddress.text = "%.3f %.3f".format(emergency.x, emergency.y)
//        coordinateToAddress(emergency.x, emergency.y, object : okhttp3.Callback {
//            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
//                Log.e("Network", "Error: ${e.message}")
//            }
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                val responseBody = response.body?.string()
//                Log.i("Network", "Response: $responseBody")
//                if (responseBody != null) {
//                    var address = responseBody.subSequence(
//                        responseBody.indexOf("display_name") + 15,
//                        responseBody.length
//                    )
//                    address = address.subSequence(0, address.indexOf("\""))
//                    requireActivity?.runOnUiThread {
//                        binding.tvAddress.text = address
//                    }
//                }
//            }
//        })

    }

    /**
     * It arranges buttons, if user is authenticated and the creator of the item user can edit and delete
     * else these buttons are gone
     */
    private fun arrangeButtons(){
        val token = DiskStorageManager.getKeyValue("token")
        val username = DiskStorageManager.getKeyValue("username").toString() // only creators can edit it
        if (DiskStorageManager.checkToken() and (username == emergency.created_by)) {
            binding.btnEdit.setOnClickListener {
                editEmergency()
            }
            binding.btnDelete.setOnClickListener {
                deleteEmergency()
            }
            binding.btnUpvote.setOnClickListener {
                upvoteEmergency(token)
            }
            binding.btnDownvote.setOnClickListener {
                downvoteEmergency(token)
            }
        } else {
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
            binding.btnSeeProfile.visibility = View.GONE
            binding.btnNavigate.visibility = View.GONE
        }
        binding.btnSeeProfile.setOnClickListener {
            addFragment(ProfileFragment(emergency.created_by),"ProfileFragment")
        }
        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
    }


    private var voted = false // if user change his/her some arrangements will happen with this parameter
    /**
     * It upvotes the emergency, increment upvote count, make upvote button not clickable and shows toast upvote successfully message
     * If user already upvotes that emergency it shows toast you already upvoted message
     */
    @SuppressLint("SetTextI18n")
    private fun upvoteEmergency(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("emergencies",emergency._id)
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
                        binding.tvDownVoteCount.text = emergency.downvote.toString()
                    }
                    voted = true
                    binding.btnUpvote.isClickable = false
                    binding.tvUpvoteCount.text = (emergency.upvote + 1).toString()

                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * It downvotes the emergency, increase downvote count, make downvote button not clickable and shows toast downvote successfully message
     * If user already downvote that emergency it shows toast you already downvote message
     */
    @SuppressLint("SetTextI18n")
    private fun downvoteEmergency(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("emergencies",emergency._id)
            voteViewModel.downvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Downvote it!", Toast.LENGTH_SHORT).show()
                }
                else if (it == "downvote"){
                    if (voted){
                        binding.btnUpvote.isClickable = true
                        binding.tvUpvoteCount.text = emergency.upvote.toString()
                    }
                    binding.btnDownvote.isClickable = false
                    binding.tvDownVoteCount.text = (emergency.downvote + 1).toString()
                    voted = true
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required), Toast.LENGTH_SHORT).show()
        }
    }

    private fun editEmergency(){
        val addEmergencyFragment = AddEmergencyFragment(emergencyViewModel, emergency)
        addFragment(addEmergencyFragment,"AddEmergencyFragment")
    }


    /** This function is called when user wants to delete emergency
     * If the creator of emergency and users match, user can delete emergency
     */
    private fun deleteEmergency(){
        emergencyViewModel.deleteEmergency(emergency._id)
        emergencyViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(context, "Cannot Deleted Check Logs", Toast.LENGTH_SHORT).show()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                if (isAdded) // to ensure it attached a parentFragmentManager
                    parentFragmentManager.popBackStack("EmergencyItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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