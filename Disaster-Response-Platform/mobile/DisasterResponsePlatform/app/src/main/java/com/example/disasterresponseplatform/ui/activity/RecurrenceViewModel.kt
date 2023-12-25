package com.example.disasterresponseplatform.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.RecurrenceModel
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class RecurrenceViewModel {

    private val networkManager = NetworkManager()

    private val liveDataRecurrenceResponse = MutableLiveData<RecurrenceModel.GetRecurrenceBody>()

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataRecurrenceResponse(): LiveData<RecurrenceModel.GetRecurrenceBody> = liveDataRecurrenceResponse

    fun getRecurrence(id: String) {
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.RECURRENCE,
            requestType = RequestType.GET,
            headers = headers,
            queries = null,
            id = id,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val functionName = "getRecurrenceWithID"
                    Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")
                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "$functionName Body: $rawJson")
                                val gson = Gson()
                                val recurrenceResponse = gson.fromJson(rawJson, RecurrenceModel.GetRecurrenceWithIDResponseBody::class.java)
                                if (recurrenceResponse != null) { // TODO check null
                                    Log.d("ResponseSuccess", "$functionName: $recurrenceResponse")
                                    liveDataRecurrenceResponse.postValue(recurrenceResponse.payload)
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
                            val responseCode = response.code()
                            Log.d("ResponseSuccess", "$functionName Error Body: $errorBody Response Code: $responseCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("onFailure", "Happens")
                }
            }
        )
    }

    private val createdRecurrenceID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getCreatedRecurrenceID(): LiveData<String> = createdRecurrenceID

    fun postRecurrence(postRequest: RecurrenceModel.PostRecurrenceBody) {
        val token = DiskStorageManager.getKeyValue("token")
        if (DiskStorageManager.checkToken()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val functionName = "postRecurrence"
            Log.d("requestBody", "$functionName  $json")
            networkManager.makeRequest(
                endpoint = Endpoint.RECURRENCE,
                requestType = RequestType.POST,
                headers = headers,
                requestBody = requestBody,
                callback = object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                        Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")

                        if (response.isSuccessful) {
                            val rawJson = response.body()?.string()
                            if (rawJson != null) {
                                try {
                                    Log.d("ResponseSuccess", "Body: $rawJson")
                                    val gson = Gson()
                                    val recurrenceResponse = gson.fromJson(rawJson, RecurrenceModel.PostRecurrenceResponseBody::class.java)
                                    val recurrenceID = recurrenceResponse.recurrence_id
                                    Log.i("Created Recurrence ", "$functionName ID: $recurrenceID ")
                                    val currentID = "" + recurrenceID
                                    createdRecurrenceID.postValue(currentID)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    createdRecurrenceID.postValue("-1")
                                    Log.e(
                                        "ResponseError",
                                        "$functionName Error reading response body: ${e.message}"
                                    )
                                }
                            } else {
                                createdRecurrenceID.postValue("-1")
                                Log.d("ResponseSuccess", "$functionName Body is null")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                createdRecurrenceID.postValue("-1")
                                Log.d(
                                    "ResponseSuccess",
                                    "$functionName Body: $errorBody Response Code: $responseCode"
                                )
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("onFailure", "Post Need Request")
                    }
                }
            )
        }
    }

    private val liveDataIsDeleted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataIsDeleted(): LiveData<Boolean> = liveDataIsDeleted

    /**
     * It deletes need with respect to ID, if it deletes successful, it post a value into livedata to notify UI
     */
    fun deleteNeed(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val networkManager = NetworkManager()
        networkManager.makeRequest(
            endpoint = Endpoint.NEED,
            requestType = RequestType.DELETE,
            id = ID,
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
                        liveDataIsDeleted.postValue(true)
                    } else {
                        liveDataIsDeleted.postValue(false)
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("no", errorBody)
                        }
                    }
                }
            })
    }

}