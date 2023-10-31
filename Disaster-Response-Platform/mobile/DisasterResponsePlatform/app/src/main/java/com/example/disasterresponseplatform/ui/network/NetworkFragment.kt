package com.example.disasterresponseplatform.ui.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.MainActivity
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.databinding.FragmentNetworkBinding
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkFragment : Fragment() {

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
                callback = object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Status Code: ${response.code()}, Successful Response!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Status Code: ${response.code()}, Error Message: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }

}