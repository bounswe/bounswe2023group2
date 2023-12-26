package com.example.disasterresponseplatform.ui.activity.need

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentNeedItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.report.ReportBottomSheetFragment
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import okhttp3.OkHttpClient
import okhttp3.Request


class NeedItemFragment(private val needViewModel: NeedViewModel, private val need: NeedBody.NeedItem) : Fragment() {

    private lateinit var binding: FragmentNeedItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorNeed)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorNeed)

        binding = FragmentNeedItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        fillTexts(need)
        arrangeButtons()
    }

    private fun fillTexts(need: NeedBody.NeedItem){
        val creatorName = need.created_by
        binding.tvCreator.text = creatorName
        binding.tvType.text = need.type
        binding.tvSubType.text = need.details["subtype"]
        binding.tvInitialQuantity.text = need.initialQuantity.toString()
        binding.tvUnsuppliedQuantity.text = need.unsuppliedQuantity.toString()
        binding.tvUrgency.text = need.urgency.toString()
        binding.tvAddress.text = need.open_address
        // if location of need is selected instead of entering open address
        if (need.x != 0.0 && need.y != 0.0){
            coordinateToAddress(need.x, need.y, object : okhttp3.Callback {
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
        binding.tvLastUpdatedTime.text = need.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = need.created_at.substring(0,10)
        binding.tvDescription.text = need.description.toString()
        //binding.e.text = need.details["subtype"]
        fillDetails(need.details)
        fillRecurrence(need)
        userViewModel.getUserRole(creatorName)
        userViewModel.getLiveDataUserRole().observe(requireActivity!!){
            val userRole = if (it == "null") "AUTHENTICATED" else it
            binding.tvUserRole.text = userRole
        }
    }

    /**
     * It opens new linear layouts in xml and fills them with respect to data fetched from backend
     * TODO do this for recurrence
     */
    @SuppressLint("SetTextI18n")
    private fun fillDetails(details: MutableMap<String,String>){
        val linearLayout = binding.layDetails
        // Loop through each key-value pair in the map
        for ((fieldName, value) in details) {
            if (fieldName != "subtype"){
                val textView = TextView(requireContext())
                textView.text = "$fieldName: $value"
                textView.textSize = 16F
                linearLayout.addView(textView)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                textView.layoutParams = layoutParams
            }
        }
    }

    /**
     * This function fills the recurrence if the need is recurrent
     */
    @SuppressLint("SetTextI18n")
    private fun fillRecurrence(need: NeedBody.NeedItem){
        val linearLayout = binding.layDetails
        if (need.occur_at != null){
            addLayoutWithMessage(linearLayout,"Occur At: ${need.occur_at.substring(0,10)}")
        }
        if (need.recurrence_rate != null){
            addLayoutWithMessage(linearLayout,"Recurrence Rate: ${need.recurrence_rate}")
        }
        if (need.recurrence_deadline != null){
            addLayoutWithMessage(linearLayout,"Recurrence Deadline: ${need.recurrence_deadline.substring(0,10)}")
        }
    }

    /**
     * It adds the textView with the message corresponding linear Layout
     */
    private fun addLayoutWithMessage(linearLayout: LinearLayout,message: String){
        val textView = TextView(requireContext())
        textView.text = message
        textView.textSize = 16F
        linearLayout.addView(textView)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
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

            if (username == need.created_by) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener {
                    editNeed()
                }
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    deleteNeed()
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
            addFragment(ProfileFragment(need.created_by),"ProfileFragment")
        }

        // Arrange vote buttons and set on click listener
        arrangeVoteButtons(token)
        binding.btnUpvote.setOnClickListener {
            voteNeed(token, "up")
        }
        binding.btnDownvote.setOnClickListener {
            voteNeed(token,"down")
        }

        binding.btnReport.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun arrangeVoteButtons(token: String?) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if (!token.isNullOrEmpty()) {
            voteViewModel.checkvote("needs", need._id)
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
     * It upvotes/downvotes/unvotes the need, arranges the buttons and shows toast accordingly
     */
    @SuppressLint("SetTextI18n")
    private fun voteNeed(token: String?, type: String) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if ((!token.isNullOrEmpty()) && (isAdded)){

            // if already upvoted, UNvote and arrange button
            if ((type == "up") && (btnUpvote.isChecked)) {
                showToast("Your upvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("needs", need._id))
                btnUpvote.isChecked = false
            }
            // if already downvoted, UNvote and arrange button
            else if ((type == "down") && (btnDownvote.isChecked)) {
                showToast("Your downvote has been withdrawn.")
                voteViewModel.unvote(VoteBody.VoteRequestBody("needs", need._id))
                btnDownvote.isChecked = false
            }
            // if not upvoted, upvote and arrange buttons
            else if ((type == "up") && !(btnUpvote.isChecked)) {
                showToast("Your upvote has been saved.")
                voteViewModel.upvote(VoteBody.VoteRequestBody("needs",need._id))
                btnUpvote.isChecked = true
                btnDownvote.isChecked = false
            }
            // if not downvoted, downvote and arrange buttons
            else if ((type == "down") && !(btnDownvote.isChecked)) {
                showToast("Your downvote has been saved.")
                voteViewModel.downvote(VoteBody.VoteRequestBody("needs",need._id))
                btnUpvote.isChecked = false
                btnDownvote.isChecked = true
            }

        } else if ((token.isNullOrEmpty()) && (isAdded)) {
            showToast(getString(R.string.pr_login_required))
            btnUpvote.isChecked = false
            btnDownvote.isChecked = false
        }
    }


    /** This function is called when user wants to edit need
     * If the creator of need and users match, user can edit need
     */
    private fun editNeed(){
        val addNeedFragment = AddNeedFragment(needViewModel,need)
        addFragment(addNeedFragment,"AddNeedFragment")
    }

    /** This function is called when user wants to delete need
     * If the creator of need and users match, user can delete need
     */
    private fun deleteNeed(){
        needViewModel.deleteNeed(need._id)
        needViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(context, "Cannot Deleted Check Logs", Toast.LENGTH_SHORT).show()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                if (isAdded) // to ensure it attached a parentFragmentManager
                    parentFragmentManager.popBackStack("NeedItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }, 200)
        }
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = ReportBottomSheetFragment(need._id, "needs")
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}