package com.example.disasterresponseplatform.ui.activity.resource

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
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentResourceItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import okhttp3.OkHttpClient
import okhttp3.Request

class ResourceItemFragment(private val resourceViewModel: ResourceViewModel, private val resource: ResourceBody.ResourceItem) : Fragment() {

    private lateinit var binding: FragmentResourceItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorResource)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorResource)

        binding = FragmentResourceItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        fillTexts(resource)
        arrangeButtons()
    }

    @SuppressLint("SetTextI18n")
    private fun fillTexts(resource: ResourceBody.ResourceItem){
        val creatorName = resource.created_by
        binding.tvCreator.text = creatorName
        binding.tvType.text = resource.type
        binding.tvSubType.text = resource.details["subtype"]
        binding.tvInitialQuantity.text = resource.initialQuantity.toString()
        binding.tvCurrentQuantity.text = resource.currentQuantity.toString()
        coordinateToAddress(resource.x, resource.y, object : okhttp3.Callback {
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
        binding.tvLastUpdatedTime.text = resource.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = resource.created_at.substring(0,10)
        binding.tvUpvoteCount.text = resource.upvote.toString()
        binding.tvDownVoteCount.text = resource.downvote.toString()
        binding.tvDescription.text = resource.description.toString()
        fillDetails(resource.details)
        fillRecurrence(resource)
        userViewModel.getUserRole(creatorName)
        userViewModel.getLiveDataUserRole().observe(requireActivity!!){
            val userRole = if (it == "null") "AUTHENTICATED" else it
            binding.tvUserRole.text = "User Role: $userRole"
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
    private fun fillRecurrence(resource: ResourceBody.ResourceItem){
        val linearLayout = binding.layDetails
        if (resource.occur_at != null){
            addLayoutWithMessage(linearLayout,"Occur At: ${resource.occur_at.substring(0,10)}")
        }
        if (resource.recurrence_rate != null){
            addLayoutWithMessage(linearLayout,"Recurrence Rate: ${resource.recurrence_rate}")
        }
        if (resource.recurrence_deadline != null){
            addLayoutWithMessage(linearLayout,"Recurrence Deadline: ${resource.recurrence_deadline.substring(0,10)}")
        }
    }

    /**
     * It adds the textView with the message corresponding linear Layout
     */
    private fun addLayoutWithMessage(linearLayout: LinearLayout, message: String){
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
        if (!token.isNullOrEmpty() and (username == resource.created_by)) {
            binding.btnEdit.setOnClickListener {
                editResource()
            }
            binding.btnDelete.setOnClickListener {
                deleteResource()
            }
        } else{
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }
        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
        binding.btnSeeProfile.setOnClickListener {
            addFragment(ProfileFragment(resource.created_by),"ProfileFragment")
        }
        binding.btnUpvote.setOnClickListener {
            upvoteResource(token)
        }
        binding.btnDownvote.setOnClickListener {
            downvoteResource(token)
        }
    }

    private var voted = false // if user change his/her some arrangements will happen with this parameter
    /**
     * It upvotes the resource, increment upvote count, make upvote button not clickable and shows toast upvote successfully message
     * If user already upvotes that resource it shows toast you already upvoted message
     */
    @SuppressLint("SetTextI18n")
    private fun upvoteResource(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("resources",resource._id)
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
                        binding.tvDownVoteCount.text = resource.downvote.toString()
                    }
                    voted = true
                    binding.btnUpvote.isClickable = false
                    binding.tvUpvoteCount.text = (resource.upvote + 1).toString()
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * It downvotes the resource, increase downvote count, make downvote button not clickable and shows toast downvote successfully message
     * If user already downvote that resource it shows toast you already downvote message
     */
    @SuppressLint("SetTextI18n")
    private fun downvoteResource(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("resources",resource._id)
            voteViewModel.downvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Downvote it!",Toast.LENGTH_SHORT).show()
                }
                else if (it == "downvote"){
                    if (voted){
                        binding.btnUpvote.isClickable = true
                        binding.tvUpvoteCount.text = resource.upvote.toString()
                    }
                    voted = true
                    binding.btnDownvote.isClickable = false
                    binding.tvDownVoteCount.text = (resource.downvote + 1).toString()
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
        }
    }

    /** This function is called whenever resource is created or edited
     * If it is created resource should be null, else need should be the clicked item
     */
    private fun editResource(){
        val addResourceFragment = AddResourceFragment(resourceViewModel,resource)
        addFragment(addResourceFragment,"AddResourceFragment")
    }

    private fun deleteResource(){
        resourceViewModel.deleteResource(resource._id)
        resourceViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(context, "Cannot Deleted Check Logs", Toast.LENGTH_SHORT).show()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                if (isAdded) // to ensure it attached a parentFragmentManager
                    parentFragmentManager.popBackStack("ResourceItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
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