package com.example.disasterresponseplatform.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.PasswordChangeSend
import com.example.disasterresponseplatform.databinding.FragmentChangePasswordBinding
import com.example.disasterresponseplatform.databinding.FragmentForgotPasswordBinding
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Response


class ChangePasswordFragment(var email: String) : Fragment(R.layout.fragment_change_password) {

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)

        binding.apply {

            verifyCode.setOnClickListener {
                val code = verificationCode.text.toString()
                val password = newPassword.text.toString()
                val confirmPassword = newPasswordAgain.text.toString()

                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                } else if (password.length < 8) {
                    Toast.makeText(context, "Password must be at least 8 characters!", Toast.LENGTH_SHORT).show()
                } else {
                    val networkManager = NetworkManager()
                    networkManager.makeRequest(
                        endpoint = Endpoint.PASSWORD_RESET_VERIFY,
                        requestType = RequestType.POST,
                        headers = mapOf("Content-Type" to "application/json"),
                        queries = mapOf("email" to email, "token" to code),
                        requestBody = Gson().toJson(PasswordChangeSend(password)).toRequestBody("application/json".toMediaTypeOrNull()),
                        callback = object : retrofit2.Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Password changed!", Toast.LENGTH_SHORT).show()
                                    parentFragmentManager.popBackStack()
                                } else {
                                    Toast.makeText(context, "Verification code is wrong!", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }

            resend.setOnClickListener {
                val url = "http://3.218.226.215:8000/api/forgot_password/send?email=$email"
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .post("".toRequestBody("application/json".toMediaTypeOrNull()))
                    .build()
                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        requireActivity().runOnUiThread {
                            Toast.makeText( requireContext(), "No internet connection", Toast.LENGTH_SHORT ).show()
                        }
                    }

                    override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                        if (response.isSuccessful) {
                            requireActivity().runOnUiThread {
                                Toast.makeText( requireContext(), "Code sent!", Toast.LENGTH_SHORT ).show()
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                Toast.makeText( requireContext(), "Email could not sent", Toast.LENGTH_SHORT ).show()
                            }
                        }
                    }
                })

            }
        }
        return binding.root
    }

}