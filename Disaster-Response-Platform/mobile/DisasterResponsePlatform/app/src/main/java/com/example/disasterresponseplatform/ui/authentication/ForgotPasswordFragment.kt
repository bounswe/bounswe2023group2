package com.example.disasterresponseplatform.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.Language
import com.example.disasterresponseplatform.databinding.FragmentForgotPasswordBinding
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Response


class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val networkManager = NetworkManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendCode.setOnClickListener {
            val email = binding.email.text.toString()
            println("Sending email...")

            val url = "http://3.218.226.215:8000/api/forgot_password/send?email=$email"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post("".toRequestBody("application/json".toMediaTypeOrNull()))
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    println("Response")
                    println(response.code)
                    println(response.message)
                    println(response.body)
                    if (response.isSuccessful) {
                        val changePasswordFragment = ChangePasswordFragment(email)
                        addFragment(changePasswordFragment)
                    } else {
                        Toast.makeText(requireContext(), "Email could not sent", Toast.LENGTH_SHORT).show()
                    }
                }
            })

//            networkManager.makeRequest(
//                endpoint = Endpoint.PASSWORD_RESET_SEND,
//                requestType = RequestType.POST,
//                headers = mapOf("Content-Type" to "application/json"),
//                queries = mapOf("email" to email),
//                callback = object : retrofit2.Callback<ResponseBody> {
//                    override fun onResponse(
//                        call: Call<ResponseBody>,
//                        response: Response<ResponseBody>
//                    ) {
//                        if (response.isSuccessful) {
//                            println("Successful")
//                        } else {
//                            println("Unsuccessful")
//                            Toast.makeText(context,"Email could not sent", Toast.LENGTH_SHORT).show()
//                            println(response.message())
//                            println(response.body())
//                            println(response.errorBody())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        println("Failure")
//                        t.printStackTrace()
//                    }
//                }
//            )
        }
    }

    private fun addFragment(fragment: Fragment) {
        parentFragmentManager.popBackStack()
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

}