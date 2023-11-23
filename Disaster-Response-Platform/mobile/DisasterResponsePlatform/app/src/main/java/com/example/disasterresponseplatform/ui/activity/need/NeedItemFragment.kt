package com.example.disasterresponseplatform.ui.activity.need

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.databinding.FragmentNeedItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager


class NeedItemFragment(private val needViewModel: NeedViewModel, private val need: Need) : Fragment() {

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

    private fun fillTexts(need: Need){
        binding.etCreatedBy.text = need.creatorName
        binding.etType.text = need.type.toString()
        binding.etInitialQuantity.text = need.quantity.toString()
        binding.etUnSuppliedQuantity.text = need.quantity.toString()
        binding.etUrgency.text = need.urgency.toString()
        binding.etCoordinateX.text = need.coordinateX.toString()
        binding.etCoordinateY.text = need.coordinateY.toString()
        binding.etDetails.text = need.details
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
        if (!token.isNullOrEmpty() and (username == need.creatorName)) {
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