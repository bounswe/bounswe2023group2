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
import com.example.disasterresponseplatform.ui.activity.VoteViewModel
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
        binding.tvAddress.text = "%.3f %.3f".format(need.x, need.y)
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
        binding.tvLastUpdatedTime.text = need.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = need.created_at.substring(0,10)
        binding.tvUpvoteCount.text = need.upvote.toString()
        binding.tvDownVoteCount.text = need.downvote.toString()
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
        if (!token.isNullOrEmpty() and (username == need.created_by)) {
            binding.btnEdit.setOnClickListener {
                editNeed()
            }
            binding.btnDelete.setOnClickListener {
                deleteNeed()
            }
        } else{
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }
        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
        binding.btnSeeProfile.setOnClickListener {
            addFragment(ProfileFragment(need.created_by),"ProfileFragment")
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
            val votePostRequest = VoteBody.VoteRequestBody("needs",need._id)
            voteViewModel.upvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Upvote it!",Toast.LENGTH_SHORT).show()
                }
                else if (it == "upvote"){
                    // if users vote for downvote before (if vote for upvote he can't click again because its not clickable)
                    if (voted){
                        binding.btnDownvote.isClickable = true
                        binding.tvDownVoteCount.text = need.downvote.toString()
                    }
                    voted = true
                    binding.btnUpvote.isClickable = false
                    binding.tvUpvoteCount.text = (need.upvote + 1).toString()

                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * It downvotes the need, increase downvote count, make downvote button not clickable and shows toast downvote successfully message
     * If user already downvote that need it shows toast you already downvote message
     */
    @SuppressLint("SetTextI18n")
    private fun downvoteNeed(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("needs",need._id)
            voteViewModel.downvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Downvote it!",Toast.LENGTH_SHORT).show()
                }
                else if (it == "downvote"){
                    if (voted){
                        binding.btnUpvote.isClickable = true
                        binding.tvUpvoteCount.text = need.upvote.toString()
                    }
                    binding.btnDownvote.isClickable = false
                    binding.tvDownVoteCount.text = (need.downvote + 1).toString()
                    voted = true
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
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