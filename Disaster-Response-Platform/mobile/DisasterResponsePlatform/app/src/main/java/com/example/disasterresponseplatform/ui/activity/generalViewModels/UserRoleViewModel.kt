package com.example.disasterresponseplatform.ui.activity.generalViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.UserBody
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserRoleViewModel {


    val networkManager = NetworkManager()
    private val liveDataMessage = MutableLiveData<UserBody.UserMessageModel>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataMessage(): LiveData<UserBody.UserMessageModel> = liveDataMessage

    /**
     * This function returns if user role is credible for requested user
     */
    fun isUserRoleCredible(userName: String?){
        if (DiskStorageManager.checkToken()){ //if authenticated user
            val headers = mapOf(
                "Authorization" to "bearer ${DiskStorageManager.getKeyValue("token")}",
                "Content-Type" to "application/json"
            )
            networkManager.makeRequest(
                endpoint = Endpoint.GETUSER,
                requestType = RequestType.GET,
                headers = headers,
                id = userName,
                callback = object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val functionName = "isUserRoleCredible"
                        Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                        Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")
                        if (response.isSuccessful) {
                            val rawJson = response.body()?.string()
                            if (rawJson != null) {
                                try {
                                    Log.d("ResponseSuccess", "$functionName Body: $rawJson")
                                    val gson = Gson()
                                    val userResponse = gson.fromJson(rawJson, UserBody.responseBody::class.java)
                                    if (userResponse != null) {
                                        Log.d("ResponseSuccess", "$functionName needResponse: $userResponse")
                                        val isCredible = userResponse.user_role=="CREDIBLE"
                                        Log.d("ResponseSuccess","$functionName username: $userName isCredible: $isCredible")
                                        liveDataMessage.postValue(UserBody.UserMessageModel(userName,isCredible))
                                    }
                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    Log.e("ResponseError", "$functionName Error reading response body: ${e.message}")
                                }
                            } else {
                                Log.d("ResponseSuccess", "$functionName Body is null")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                Log.d("ResponseSuccess", "$functionName Body: $errorBody")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("onFailure", "Happens")
                    }
                }
            )
        }

    }

}

