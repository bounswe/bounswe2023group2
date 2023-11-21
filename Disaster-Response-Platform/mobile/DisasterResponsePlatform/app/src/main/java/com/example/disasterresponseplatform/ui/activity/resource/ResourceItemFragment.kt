package com.example.disasterresponseplatform.ui.activity.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.databinding.FragmentResourceItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager

class ResourceItemFragment(private val resourceViewModel: ResourceViewModel, private val resource: Resource) : Fragment() {

    private lateinit var binding: FragmentResourceItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResourceItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillTexts()
        arrangeButtons()
    }

    private fun fillTexts(){
        binding.etCreatedBy.text = resource.creatorName
        binding.etType.text = resource.type.toString()
        binding.etInitialQuantity.text = resource.quantity.toString()
        binding.etUnSuppliedQuantity.text = resource.quantity.toString()
        binding.etCondition.text = resource.condition.toString()
        binding.etCoordinateX.text = resource.coordinateX.toString()
        binding.etCoordinateY.text = resource.coordinateY.toString()
        binding.etDetails.text = resource.details
    }

    private fun arrangeButtons(){
        binding.btnEdit.setOnClickListener {
            editResource()
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

    /** This function is called whenever resource is created or edited
     * If it is created need should be null, else need should be the clicked item
     */
    private fun editResource(){
        val token = DiskStorageManager.getKeyValue("token")
        val username = DiskStorageManager.getKeyValue("username").toString()
        if (!token.isNullOrEmpty() and (username == resource.creatorName)) {
            val addResourceFragment = AddResourceFragment(resourceViewModel,resource)
            addFragment(addResourceFragment,"AddResourceFragment")
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