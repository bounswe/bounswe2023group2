package com.example.disasterresponseplatform.ui.network

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.databinding.FragmentNetworkBinding
import com.example.disasterresponseplatform.managers.DataResponse
import com.example.disasterresponseplatform.managers.NetworkManager
import retrofit2.Call
import retrofit2.Response


class NetworkFragment(val mainActivity: MainActivity) : Fragment() {

    private lateinit var binding: FragmentNetworkBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNetworkBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButton()
    }

    private fun clickButton(){
        binding.btNetwork.setOnClickListener {
            // Handle the button click event

            val networkManager = NetworkManager()

            val headers = mapOf("Authorization" to "Bearer YOUR_TOKEN")

            networkManager.makeRequest(
                endpoint = Endpoint.DATA,
                requestType = RequestType.GET,  // Specify the type of request here
                headers = headers,
                callback = object : retrofit2.Callback<DataResponse> {
                    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                        Toast.makeText(mainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(mainActivity, "Status Code: ${response.code()}, Successful Response!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(mainActivity, "Status Code: ${response.code()}, Error Message: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }

}