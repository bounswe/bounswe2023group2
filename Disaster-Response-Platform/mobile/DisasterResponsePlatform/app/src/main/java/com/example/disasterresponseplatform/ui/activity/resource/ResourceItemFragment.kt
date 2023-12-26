package com.example.disasterresponseplatform.ui.activity.resource

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
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentResourceItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.report.ReportBottomSheetFragment
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import com.example.disasterresponseplatform.utils.Annotation
import com.example.disasterresponseplatform.utils.UserRoleUtil
import okhttp3.OkHttpClient
import okhttp3.Request

class ResourceItemFragment(private val resourceViewModel: ResourceViewModel, private val resource: ResourceBody.ResourceItem) : Fragment() {

    private lateinit var binding: FragmentResourceItemBinding
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
        annotation.getAnnotations(resource.type, {annotationText ->
            binding.tvType.setOnLongClickListener {
                Toast(context).also {
                    val view = LayoutInflater.from(context).inflate(R.layout.annotation_layout, null)
                    val background = view.findViewById<LinearLayout>(R.id.annotationBackground)
                    val tvWord = view.findViewById<TextView>(R.id.tvWord)
                    val tvAnnotation = view.findViewById<TextView>(R.id.tvAnnotation)
                    background.background = ContextCompat.getDrawable(requireContext(), R.color.colorResource)
                    tvWord.text = resource.type
                    tvAnnotation.text = annotationText
                    it.setView(view)
                    it.duration = Toast.LENGTH_LONG
                    it.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 350)
                }.show()
                false
            }
        }, {})
        binding.tvSubType.text = resource.details["subtype"]
        annotation.getAnnotations(resource.details["subtype"] ?: "", {annotationText ->
            binding.tvSubType.setOnLongClickListener {
                Toast(context).also {
                    val view = LayoutInflater.from(context).inflate(R.layout.annotation_layout, null)
                    val background = view.findViewById<LinearLayout>(R.id.annotationBackground)
                    val tvWord = view.findViewById<TextView>(R.id.tvWord)
                    val tvAnnotation = view.findViewById<TextView>(R.id.tvAnnotation)
                    background.background = ContextCompat.getDrawable(requireContext(), R.color.colorResource)
                    tvWord.text = resource.details["subtype"]
                    tvAnnotation.text = annotationText
                    it.setView(view)
                    it.duration = Toast.LENGTH_LONG
                    it.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 350)
                }.show()
                false
            }
        }, {})
        binding.tvInitialQuantity.text = resource.initialQuantity.toString()
        binding.tvCurrentQuantity.text = resource.currentQuantity.toString()
        if (resource.x != 0.0 && resource.y != 0.0){
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
        }

        binding.tvLastUpdatedTime.text = resource.last_updated_at?.substring(0,10)
        binding.tvCreationTime.text = resource.created_at.substring(0,10)
        if (resource.details["subtype"] != null)
            annotation.getAnnotations(resource.created_by + "-resource-" + resource.details["subtype"], {
                binding.tvDescription.text = it
            }, {
                binding.tvDescription.text = resource.description.toString()
            })
        else binding.tvDescription.text = resource.description.toString()
        fillDetails(resource.details)
        fillRecurrence(resource)
        val userRole = UserRoleUtil.getUserRole(creatorName)
        binding.tvUserRole.text = userRole
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
     * This function fills the recurrence if the resource is recurrent
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

            if (username == resource.created_by) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener {
                    editResource()
                }
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnDelete.setOnClickListener {
                    deleteResource()
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
            addFragment(ProfileFragment(resource.created_by),"ProfileFragment")
        }

        // Arrange vote buttons and set on click listener
        arrangeVoteButtons(token)
        binding.btnUpvote.setOnClickListener {
            voteResource(token, "up")
        }
        binding.btnDownvote.setOnClickListener {
            voteResource(token,"down")
        }

        binding.btnReport.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun arrangeVoteButtons(token: String?) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote

        if (!token.isNullOrEmpty()) {
            voteViewModel.checkvote("resources", resource._id)
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
     * It upvotes/downvotes/unvotes the resource, arranges the buttons and shows toast accordingly
     */
    @SuppressLint("SetTextI18n")
    private fun voteResource(token: String?, type: String) {

        val btnUpvote = binding.btnUpvote
        val btnDownvote = binding.btnDownvote
        val resourceID = resource._id

        if (isAdded) {
            when {
                token.isNullOrEmpty() -> {
                    showToast(getString(R.string.pr_login_required))
                    binding.btnUpvote.isChecked = false
                    binding.btnDownvote.isChecked = false
                }
                // if already upvoted, UNvote and arrange button
                type == "up" && btnUpvote.isChecked -> {
                    showToast("Your upvote has been withdrawn.")
                    voteViewModel.unvote(VoteBody.VoteRequestBody("resources", resourceID))
                    btnUpvote.isChecked = false
                }
                // if already downvoted, UNvote and arrange button
                type == "down" && btnDownvote.isChecked -> {
                    showToast("Your downvote has been withdrawn.")
                    voteViewModel.unvote(VoteBody.VoteRequestBody("resources", resourceID))
                    btnDownvote.isChecked = false
                }
                // if not upvoted, upvote and arrange buttons
                type == "up" -> {
                    showToast("Your upvote has been saved.")
                    voteViewModel.upvote(VoteBody.VoteRequestBody("resources", resourceID))
                    btnUpvote.isChecked = true
                    btnDownvote.isChecked = false
                }
                // if not downvoted, downvote and arrange buttons
                type == "down" -> {
                    showToast("Your downvote has been saved.")
                    voteViewModel.downvote(VoteBody.VoteRequestBody("resources", resourceID))
                    btnUpvote.isChecked = false
                    btnDownvote.isChecked = true
                }
            }
        }
    }

    /** This function is called whenever resource is created or edited
     * If it is created resource should be null, else resource should be the clicked item
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
    private fun showBottomSheet() {
        val bottomSheetFragment = ReportBottomSheetFragment(resource._id, "resources")
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