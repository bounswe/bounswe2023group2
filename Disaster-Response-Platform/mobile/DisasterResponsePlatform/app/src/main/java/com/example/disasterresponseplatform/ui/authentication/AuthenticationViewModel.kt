package com.example.disasterresponseplatform.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.managers.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel@Inject constructor() : ViewModel() {

    // Mutable LiveData for username and password
    private val _username = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    private val _signUpFullName = MutableLiveData<String>()
    private val _signUpUsername = MutableLiveData<String>()
    private val _signUpEmail = MutableLiveData<String>()
    private val _signUpPassword = MutableLiveData<String>()
    private val _signUpConfirmPassword = MutableLiveData<String>()


    private val networkManager = NetworkManager()

    // LiveData to hold any error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Validation result
    private val _loginValidation = MutableLiveData<Boolean>()
    val loginValidation: LiveData<Boolean> get() = _loginValidation

    private val _signUpValidation = MutableLiveData<Int>()
    val signUpValidation: LiveData<Int> get() = _signUpValidation

    // Update username and password from the UI
    fun updateSignUpFullName(signUpFullName: String) {
        _signUpFullName.value = signUpFullName
    }

    fun updateSignUpUsername(signUpUsername: String) {
        _signUpUsername.value = signUpUsername
    }

    fun updateSignUpEmail(signUpEmail: String) {
        _signUpEmail.value = signUpEmail
    }

    fun updateSignUpPassword(signUpPassword: String) {
        _signUpPassword.value = signUpPassword
    }

    fun updateSignUpConfirmPassword(signUpConfirmPassword: String) {
        _signUpConfirmPassword.value = signUpConfirmPassword
    }

    fun updateUsername(username: String) {
        _username.value = username
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    // Validate login credentials (Dummy validation for demonstration)
    fun validateLogin() {
        Log.d("", "validated log in")
        _loginValidation.value =
            !(_username.value.isNullOrEmpty() || _password.value.isNullOrEmpty())
    }

    fun validateSignUp() {

        // Perform your validation logic here
        val fullName = _signUpFullName.value
        val username = _signUpUsername.value
        val email = _signUpEmail.value
        val password = _signUpPassword.value
        val confirmPassword = _signUpConfirmPassword.value

        // Check if any of the fields are empty or null
        if (fullName.isNullOrEmpty() || username.isNullOrEmpty() ||
            email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()
        ) {
            _signUpValidation.value = 0
            return
        }

        // Check if the password and confirm password match
        if (password != confirmPassword) {
            _signUpValidation.value = 1
            return
        }

        // If all checks pass, mark the data as valid
        _signUpValidation.value = 2
    }

    fun sendSignInRequest() {
        val headers = mapOf(
            // Not needed for log in
            //"Authorization" to "Bearer YOUR_ACCESS_TOKEN",
            "Content-Type" to "application/json"
        )

        val requestBody: Map<String, Any> = mapOf(
            "username" to _username.value!!,
            "password" to _password.value!!,
            // ... add as many key-value pairs as needed
        )
        networkManager.makeRequest(
            endpoint = Endpoint.DATA,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : Callback<Response<ResponseBody>> {
                override fun onResponse(
                    call: Call<Response<ResponseBody>>,
                    response: Response<Response<ResponseBody>>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val responseBody = response.body()?.body() // Access the inner ResponseBody
                        if (responseBody != null) {
                            try {
                                val content = responseBody.string()

                                Log.d("ResponseSuccess", "Body: $content")
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e("ResponseError", "Error reading response body: ${e.message}")
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        Log.d("ResponseError", "Error Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Response<ResponseBody>>, t: Throwable) {
                    // Handle the failure case
                }
            }
        )

    }

    fun sendSignUpRequest() {
        val headers = mapOf(
            // Not needed for Sign Up
            //"Authorization" to "Bearer YOUR_ACCESS_TOKEN",
            "Content-Type" to "application/json"
        )

        val requestBody: Map<String, Any> = mapOf(
            "fullname" to _signUpFullName!!,
            "username" to _signUpUsername,
            "email" to _signUpEmail,
            "password" to _signUpPassword,
            "passwordConfirmed" to _signUpConfirmPassword,

            // ... add as many key-value pairs as needed
        )
        networkManager.makeRequest(
            endpoint = Endpoint.DATA,
            requestType = RequestType.POST,
            headers = headers,
            requestBody = requestBody,
            callback = object : Callback<Response<ResponseBody>> {
                override fun onResponse(
                    call: Call<Response<ResponseBody>>,
                    response: Response<Response<ResponseBody>>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val responseBody = response.body()?.body() // Access the inner ResponseBody
                        if (responseBody != null) {
                            try {
                                val content = responseBody.string()
                                Log.d("ResponseSuccess", "Body: $content")
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e("ResponseError", "Error reading response body: ${e.message}")
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        Log.d("ResponseError", "Error Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Response<ResponseBody>>, t: Throwable) {
                    // Handle the failure case
                }
            }
        )
    }
}

