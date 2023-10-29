package com.example.disasterresponseplatform.ui.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.RegisterRequestBody
import com.example.disasterresponseplatform.data.models.authModels.SignInRequestBody
import com.example.disasterresponseplatform.data.models.authModels.SignUpResponseBody
import com.example.disasterresponseplatform.data.models.authModels.ValidationErrorResponse
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel@Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
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

    private val _signUpError = MutableLiveData<String?>()
    val signUpError: LiveData<String?> = _signUpError

    private val _signUpSuccessful = MutableLiveData<Boolean>()
    val signUpSuccessful: LiveData<Boolean> = _signUpSuccessful

    private val _signInError = MutableLiveData<String?>()
    val signInError: LiveData<String?> = _signInError

    private val _signInSuccessful = MutableLiveData<Boolean>()
    val signInSuccessful: LiveData<Boolean> = _signInSuccessful

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
    fun updateEmail(email: String) {
        _email.value = email
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
            "Content-Type" to "application/json"
        )

        val signInRequestBody = _username.value?.let {
            _password.value?.let { it1 ->
                _email.value?.let { it2 ->
                    SignInRequestBody(
                        username = it,
                        email = it2,
                        password = it1,
                        disabled = true
                    )
                }
            }
        }

        val gson = Gson()
        val json = gson.toJson(signInRequestBody)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        Log.d("requestBody", json.toString())

        networkManager.makeRequest(
            endpoint = Endpoint.LOGIN,
            requestType = RequestType.POST,
            headers = headers,
            requestBody = requestBody,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val signUpResponse = gson.fromJson(rawJson, SignUpResponseBody::class.java)
                                Log.d("Sign In Access Token", signUpResponse.accessToken + ".")
                                DiskStorageManager.setKeyValue("token", signUpResponse.accessToken)
                                _signInSuccessful.value = true
                            } catch (e: IOException) {
                                Log.e("ResponseError", "Error reading response body: ${e.message}")
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            try {
                                val errorResponse = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
                                Log.d("Error Message", errorResponse.detail[0].message)
                                _signInError.value = errorResponse.detail[0].message
                            } catch (e: JsonSyntaxException) {
                                Log.e("ResponseError", "Error parsing error body: ${e.message}")
                            }
                        }
                        Log.d("ResponseError", "Error Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("ResponseError", "Request failed: ${t.message}")
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

        // Safely unwrap the values
        val username = _signUpUsername.value ?: "defaultUsername"
        val email = _signUpEmail.value ?: "defaultEmail@example.com"
        val password = _signUpPassword.value ?: "defaultPassword"
        val fullName = _signUpFullName.value ?: "Default Name"
        val phoneNumber = "05346710755" ?: "defaultPhoneNumber"

        // Splitting the full name into first name and last name
        val parts = fullName.split(" ", limit = 2)
        val firstName = parts.getOrElse(0) { "" }
        val lastName = parts.getOrElse(1) { "" }

        val registerRequestBody = RegisterRequestBody(
            username = username,
            email = email,
            disabled = true,
            password = password,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            isEmailVerified = false,
            privateAccount = false
        )
        val gson = Gson()
        val json = gson.toJson(registerRequestBody)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        Log.d("requestBody", json.toString())

        networkManager.makeRequest(
            endpoint = Endpoint.SIGNUP,
            requestType = RequestType.POST,
            headers = headers,
            requestBody = requestBody,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfo", "Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val signUpResponse = gson.fromJson(rawJson, SignUpResponseBody::class.java)
                                Log.d("Access Token", signUpResponse.accessToken + "asdas")
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e("ResponseError", "Error reading response body: ${e.message}")
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            try {
                                val gson = Gson()
                                val errorResponse = gson.fromJson(errorBody, ValidationErrorResponse::class.java)
                                Log.d("Error Message", errorResponse.detail[0].message)
                                _signUpError.value = errorResponse.detail[0].message
                            } catch (e: JsonSyntaxException) {
                                // Handle JSON parsing error
                                Log.e("ResponseError", "Error parsing error body: ${e.message}")
                            }
                        }

                        if (errorBody != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $errorBody")
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e("ResponseError", "Error reading response body: ${e.message}")
                            }
                        }
                        Log.d("ResponseError", "Error Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle the failure case
                }
            }
        )
    }
}

