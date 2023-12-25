package com.example.disasterresponseplatform.ui.activity.generalViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
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

    private val attachedRecurrenceID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getAttachedRecurrenceID(): LiveData<String> = attachedRecurrenceID

    fun attachRecurrence(body: RecurrenceModel.AttachActivityBody) {
        val token = DiskStorageManager.getKeyValue("token")
        if (DiskStorageManager.checkToken()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(body)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val functionName = "attachRecurrence"
            Log.d("requestBody", "$functionName  $json")
            networkManager.makeRequest(
                endpoint = Endpoint.RECURRENCE_ATTACH_ACTIVITY,
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
                                    val attachResponse = gson.fromJson(rawJson, RecurrenceModel.AttachResponseBody::class.java)
                                    val recurrenceID = attachResponse.recurrence_id
                                    Log.i("Attached Recurrence ", "$functionName ID: $recurrenceID ")
                                    val currentID = "" + recurrenceID
                                    attachedRecurrenceID.postValue(currentID)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    attachedRecurrenceID.postValue("-1")
                                    Log.e(
                                        "ResponseError",
                                        "$functionName Error reading response body: ${e.message}"
                                    )
                                }
                            } else {
                                attachedRecurrenceID.postValue("-1")
                                Log.d("ResponseSuccess", "$functionName Body is null")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                attachedRecurrenceID.postValue("-1")
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

    fun deleteRecurrence(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val functionName = "attachRecurrence"
            networkManager.makeRequest(
            endpoint = Endpoint.RECURRENCE,
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
                    Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        liveDataIsDeleted.postValue(true)
                    } else {
                        liveDataIsDeleted.postValue(false)
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("Error", "$functionName $errorBody")
                        }
                    }
                }
            })
    }

    private val isRecurrenceStarted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getIsRecurrenceStarted(): LiveData<Boolean> = isRecurrenceStarted
    fun startRecurrence(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val functionName = "startRecurrence"
        networkManager.makeRequest(
            endpoint = Endpoint.RECURRENCE_START,
            requestType = RequestType.GET,
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
                    Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        isRecurrenceStarted.postValue(true)
                    } else {
                        isRecurrenceStarted.postValue(false)
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("Error", "$functionName $errorBody")
                        }
                    }
                }
            })
    }

    private val isRecurrenceCancelled = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getIsRecurrenceCancelled(): LiveData<Boolean> = isRecurrenceCancelled
    fun cancelRecurrence(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val functionName = "cancelRecurrence"
        networkManager.makeRequest(
            endpoint = Endpoint.RECURRENCE_START,
            requestType = RequestType.GET,
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
                    Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        isRecurrenceCancelled.postValue(true)
                    } else {
                        isRecurrenceCancelled.postValue(false)
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("Error", "$functionName $errorBody")
                        }
                    }
                }
            })
    }

    private val isRecurrenceResumed = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getIsRecurrenceResumed(): LiveData<Boolean> = isRecurrenceResumed
    fun resumeRecurrence(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val functionName = "resumeRecurrence"
        networkManager.makeRequest(
            endpoint = Endpoint.RECURRENCE_RESUME,
            requestType = RequestType.GET,
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
                    Log.d("ResponseInfo", "$functionName Status Code: ${response.code()}")
                    Log.d("ResponseInfo", "$functionName Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        isRecurrenceResumed.postValue(true)
                    } else {
                        isRecurrenceResumed.postValue(false)
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            Log.d("Error", "$functionName $errorBody")
                        }
                    }
                }
            })
    }

}