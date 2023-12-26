package com.example.disasterresponseplatform.ui.activity.emergency

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
import com.example.disasterresponseplatform.utils.Annotation
import okhttp3.OkHttpClient
import okhttp3.Request

class EmergencyItemFragment(private val emergencyViewModel: EmergencyViewModel, private val emergency: EmergencyBody.EmergencyItem) : Fragment() {

    private lateinit var binding: FragmentEmergencyItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()
    private val annotation = Annotation()

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
        val creatorUsername = emergency.created_by_user

        // Check whether it is created by user or guest
        if (!creatorUsername.isNullOrEmpty()) {
            binding.tvCreator.text = creatorUsername
            userViewModel.getUserRole(creatorUsername)
            userViewModel.getLiveDataUserRole().observe(requireActivity!!) {
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
        annotation.getAnnotations(emergency.type, {annotationText ->
            binding.tvType.setOnLongClickListener {
                Toast(context).also {
                    val view = LayoutInflater.from(context).inflate(R.layout.annotation_layout, null)
                    val background = view.findViewById<LinearLayout>(R.id.annotationBackground)
                    val tvWord = view.findViewById<TextView>(R.id.tvWord)
                    val tvAnnotation = view.findViewById<TextView>(R.id.tvAnnotation)
                    background.background = ContextCompat.getDrawable(requireContext(), R.color.colorEmergency)
                    tvWord.text = emergency.type
                    tvAnnotation.text = annotationText
                    it.setView(view)
                    it.duration = Toast.LENGTH_LONG
                    it.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 350)
                }.show()
                false
            }
        }, {})
        annotation.getAnnotations(emergency.created_by_user + "-emergency-" + emergency.type, {
            binding.tvDescription.text = it
        }, {
            binding.tvDescription.text = emergency.description

        })
//        binding.tvDescription.text = emergency.description
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
        Log.i("EmergencyGelen","Created by: ${emergency.created_by_user} Username: $username")
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

            if (username == emergency.created_by_user) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener {
                    editEmergency()
                }
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    deleteEmergency()
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
            addFragment(ProfileFragment(emergency.created_by_user),"ProfileFragment")
        }

        // Arrange vote buttons and set on click listener
        arrangeVoteButtons()
        binding.btnUpvote.setOnClickListener {
            voteEmergency(token, "up")
        }
        binding.btnDownvote.setOnClickListener {
            voteEmergency(token,"down")
        }

        binding.btnReport.setOnClickListener {
            //showBottomSheet()
        }
    }


    private fun arrangeVoteButtons() {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if (DiskStorageManager.checkToken()) {
            voteViewModel.checkvote("emergencies", emergency._id)
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
     * It upvotes/downvotes/unvotes the emergency, arranges the buttons and shows toast accordingly
     */
    @SuppressLint("SetTextI18n")
    private fun voteEmergency(token: String?, type: String) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if ((!token.isNullOrEmpty()) && (isAdded)){

            // if already upvoted, UNvote and arrange button
            if (type == "up" && btnUpvote.isChecked) {
                showToast("Your upvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("emergencies", emergency._id))
                btnUpvote.isChecked = false
            }
            // if already downvoted, UNvote and arrange button
            else if (type == "down" && btnDownvote.isChecked) {
                showToast("Your downvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("emergencies", emergency._id))
                btnDownvote.isChecked = false
            }
            // if not upvoted, upvote and arrange buttons
            else if (type == "up" && !btnUpvote.isChecked) {
                showToast("Your upvote has been saved.")
                voteViewModel.upvote(VoteBody.VoteRequestBody("emergencies",emergency._id))
                btnUpvote.isChecked = true
                btnDownvote.isChecked = false
            }
            // if not downvoted, downvote and arrange buttons
            else if (type == "down" && !btnDownvote.isChecked) {
                showToast("Your downvote has been saved.")
                voteViewModel.downvote(VoteBody.VoteRequestBody("emergencies",emergency._id))
                btnUpvote.isChecked = false
                btnDownvote.isChecked = true
            }

        } else if ((token.isNullOrEmpty()) && (isAdded)) {
            showToast(getString(R.string.pr_login_required))
            btnUpvote.isChecked = false
            btnDownvote.isChecked = false
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
        val url = "https://geocode.maps.co/reverse?lat=$x&lon=$y&api_key=658a6bb850a62680253220cju871eba"
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