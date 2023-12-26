package com.example.disasterresponseplatform.ui.activity.action

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.disasterresponseplatform.data.models.ActionBody
import com.example.disasterresponseplatform.data.models.VoteBody
import com.example.disasterresponseplatform.databinding.FragmentActionItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.generalViewModels.VoteViewModel
import com.example.disasterresponseplatform.ui.activity.report.ReportBottomSheetFragment
import com.example.disasterresponseplatform.ui.activity.util.map.ActivityMap
import com.example.disasterresponseplatform.ui.authentication.UserViewModel
import com.example.disasterresponseplatform.utils.Annotation
import okhttp3.OkHttpClient
import okhttp3.Request


class ActionItemFragment(private val actionViewModel: ActionViewModel, private val action: ActionBody.ActionItem) : Fragment() {

    private lateinit var binding: FragmentActionItemBinding
    private var requireActivity: FragmentActivity? = null
    private val voteViewModel = VoteViewModel()
    private val userViewModel = UserViewModel()
    private var annotation = Annotation()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorAction)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorAction)

        binding = FragmentActionItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity == null) { // to handle error when user enters this page twice
            requireActivity = requireActivity()
        }
        fillTexts(action)
        arrangeButtons()
    }

    private fun fillTexts(action: ActionBody.ActionItem){
        val creatorName = action.created_by
        binding.etCreatedBy.text = creatorName
        binding.etType.text = action.type
        binding.tvLastUpdatedTime.text = action.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = action.created_at.substring(0,10)
        binding.tvUpvoteCount.text = action.upvote.toString()
        binding.tvDownVoteCount.text = action.downvote.toString()
        annotation.getAnnotations(action.created_by + "-action-" + action.type, {
            binding.tvDescription.text = it
        }, {
            binding.tvDescription.text = action.description
        })
        userViewModel.getUserRole(creatorName)
        userViewModel.getLiveDataUserRole().observe(requireActivity!!){
            val userRole = if (it == "null") "AUTHENTICATED" else it
            binding.etUserRole.text = userRole
        }
    }



    /**
     * It adds the textView with the message corresponding linear Layout
     */
    private fun addLayoutWithMessage(linearLayout: LinearLayout,message: String){
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
        val token = DiskStorageManager.getKeyValue("token")
        val username = DiskStorageManager.getKeyValue("username").toString() // only creators can edit it
        if (DiskStorageManager.checkToken() and (username == action.created_by)) {
            binding.btnEdit.setOnClickListener {
                editAction()
            }
            binding.btnDelete.setOnClickListener {
                deleteAction()
            }
            binding.btnUpvote.setOnClickListener {
                upvoteAction(token)
            }
            binding.btnDownvote.setOnClickListener {
                downvoteAction(token)
            }
            binding.btnReport.setOnClickListener {
                showBottomSheet()
            }
        } else{
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
            binding.btnUpvote.visibility = View.GONE
            binding.btnDownvote.visibility = View.GONE
            binding.btnReport.visibility = View.GONE
        }
        binding.btnNavigate.setOnClickListener {
            navigateToMapFragment()
        }
        binding.btnSeeProfile.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }

    }

    private var voted = false // if user change his/her some arrangements will happen with this parameter
    /**
     * It upvotes the action, increment upvote count, make upvote button not clickable and shows toast upvote successfully message
     * If user already upvotes that action it shows toast you already upvoted message
     */
    @SuppressLint("SetTextI18n")
    private fun upvoteAction(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("actions",action._id)
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
                        binding.tvDownVoteCount.text = action.downvote.toString()
                    }
                    voted = true
                    binding.btnUpvote.isClickable = false
                    binding.tvUpvoteCount.text = (action.upvote + 1).toString()

                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * It downvotes the action, increase downvote count, make downvote button not clickable and shows toast downvote successfully message
     * If user already downvote that action it shows toast you already downvote message
     */
    @SuppressLint("SetTextI18n")
    private fun downvoteAction(token: String?){
        if (!token.isNullOrEmpty()){
            val votePostRequest = VoteBody.VoteRequestBody("actions",action._id)
            voteViewModel.downvote(votePostRequest)
            voteViewModel.getLiveDataMessage().observe(requireActivity!!){
                if (it == "-1"){
                    if (isAdded)
                        Toast.makeText(requireContext(),"You Already Downvote it!",Toast.LENGTH_SHORT).show()
                }
                else if (it == "downvote"){
                    if (voted){
                        binding.btnUpvote.isClickable = true
                        binding.tvUpvoteCount.text = action.upvote.toString()
                    }
                    binding.btnDownvote.isClickable = false
                    binding.tvDownVoteCount.text = (action.downvote + 1).toString()
                    voted = true
                }
            }
        } else{
            if (isAdded)
                Toast.makeText(requireContext(),getString(R.string.pr_login_required),Toast.LENGTH_SHORT).show()
        }
    }

    /** This function is called when user wants to edit action
     * If the creator of action and users match, user can edit action
     */
    private fun editAction(){
        val addActionFragment = AddActionFragment(actionViewModel,action)
        addFragment(addActionFragment,"AddActionFragment")
    }

    /** This function is called when user wants to delete action
     * If the creator of action and users match, user can delete action
     */
    private fun deleteAction(){
        actionViewModel.deleteAction(action._id)
        actionViewModel.getLiveDataIsDeleted().observe(requireActivity!!){
            if (isAdded) { // to ensure it attached a context
                if (it){
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()

                } else{
                    Toast.makeText(context, "Cannot Deleted Check Logs", Toast.LENGTH_SHORT).show()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ // delay for not giving error because of requireActivity
                if (isAdded) // to ensure it attached a parentFragmentManager
                    parentFragmentManager.popBackStack("ActionItemFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }, 200)
        }
    }

    private fun navigateToMapFragment() {
        val mapFragment = ActivityMap()
        addFragment(mapFragment,"ActivityMap")
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = ReportBottomSheetFragment(action._id, "actions")
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
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

}