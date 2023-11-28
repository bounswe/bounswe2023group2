package com.example.disasterresponseplatform.ui.activity.resource

import android.annotation.SuppressLint
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

class ResourceItemFragment(private val resourceViewModel: ResourceViewModel, private val resource: ResourceBody.ResourceItem) : Fragment() {

    private lateinit var binding: FragmentResourceItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResourceItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts(resource)
        arrangeButtons()
    }

    private fun fillTexts(resource: ResourceBody.ResourceItem){
        binding.etCreatedBy.text = resource.created_by
        binding.etType.text = resource.type
        binding.etInitialQuantity.text = resource.initialQuantity.toString()
        binding.etUnSuppliedQuantity.text = resource.currentQuantity.toString()
        binding.etCoordinateX.text = resource.x.toString()
        binding.etCoordinateY.text = resource.y.toString()
        binding.tvLastUpdatedTime.text = resource.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = resource.created_at.substring(0,10)
        binding.tvUpvoteCount.text = resource.upvote.toString()
        binding.tvDownVoteCount.text = resource.downvote.toString()
        binding.tvDescription.text = resource.description.toString()
        binding.etSubType.text = resource.details["subtype"]
        fillDetails(resource.details)
        fillRecurrence(resource)
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
        linearLayout.addView(textView)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
    }

    /**
     * It arranges buttons, if user is authenticated and the creator of the item user can edit and delete
     * else these buttons are gone
     */
    private fun arrangeButtons(){
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
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
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(),"You need to log in!",Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(),"You need to log in!",Toast.LENGTH_SHORT).show()
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

}