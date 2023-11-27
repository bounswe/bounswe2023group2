package com.example.disasterresponseplatform.ui.activity.need

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.databinding.FragmentNeedItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager


class NeedItemFragment(private val needViewModel: NeedViewModel, private val need: NeedBody.NeedItem) : Fragment() {

    private lateinit var binding: FragmentNeedItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNeedItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts(need)
        arrangeButtons()
    }

    private fun fillTexts(need: NeedBody.NeedItem){
        binding.etCreatedBy.text = need.created_by
        binding.etType.text = need.type
        binding.etInitialQuantity.text = need.initialQuantity.toString()
        binding.etUnSuppliedQuantity.text = need.unsuppliedQuantity.toString()
        binding.etUrgency.text = need.urgency.toString()
        binding.etCoordinateX.text = need.x.toString()
        binding.etCoordinateY.text = need.y.toString()
        binding.tvLastUpdatedTime.text = need.last_updated_at.substring(0,10)
        binding.tvCreationTime.text = need.created_at.substring(0,10)
        binding.tvUpvoteCount.text = need.upvote.toString()
        binding.tvDownVoteCount.text = need.downvote.toString()
        binding.tvDescription.text = need.description.toString()
        binding.etSubType.text = need.details["Sub Type"]
        fillDetails(need.details)
        fillRecurrence(need)
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
            if (fieldName != "Sub Type"){
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
        linearLayout.addView(textView)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = layoutParams
    }

    private fun arrangeButtons(){
        binding.btnEdit.setOnClickListener {
            editNeed()
        }
        binding.btnDelete.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }
        binding.btnNavigate.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }
        binding.btnSeeProfile.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }
        binding.btnUpvote.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }
        binding.btnDownvote.setOnClickListener {
            Toast.makeText(context, "Soon", Toast.LENGTH_SHORT).show()
        }
    }

    /** This function is called whenever need is created or edited
     * If it is created need should be null, else need should be the clicked item
     */
    private fun editNeed(){
        val token = DiskStorageManager.getKeyValue("token")
        val username = DiskStorageManager.getKeyValue("username").toString() // only creators can edit it
        if (!token.isNullOrEmpty() and (username == need.created_by)) {
            val addNeedFragment = AddNeedFragment(needViewModel,need)
            addFragment(addNeedFragment,"AddNeedFragment")
        }
        else{
            Toast.makeText(context, "You don't have enough authority to edit it!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addFragment(fragment: Fragment, fragmentName: String) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(fragmentName)
        ft.commit()
    }

}