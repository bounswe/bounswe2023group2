package com.example.disasterresponseplatform.ui.activity.need

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.databinding.FragmentNeedItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            deleteNeed()
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

    private fun deleteNeed() {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        var networkManager = NetworkManager()
        networkManager.makeRequest(
            endpoint = Endpoint.NEED,
            requestType = RequestType.DELETE,
            id = need.ID,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle failure when the request fails
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                        getFragmentManager()?.popBackStack()
                    } else {
                        Toast.makeText(context, "Not Authorized", Toast.LENGTH_SHORT).show()
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("no", errorBody)
                        }
                    }
                }
            })
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