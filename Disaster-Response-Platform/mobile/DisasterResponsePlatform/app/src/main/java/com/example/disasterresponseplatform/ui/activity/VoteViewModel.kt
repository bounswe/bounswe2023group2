package com.example.disasterresponseplatform.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.VoteBody
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

//TODO make this real view model and implement DI
class VoteViewModel {

    val networkManager = NetworkManager()

    val liveDataMessage = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataMessage(): LiveData<String> = liveDataMessage

    /**
     * Up voting with respect to body
     */
    fun upvote(postRequest: VoteBody.VoteRequestBody) {
        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token", "Token $token")
        if (!token.isNullOrEmpty()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.UPVOTE,
                requestType = RequestType.PUT,
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
                                    val registerResponse = gson.fromJson(
                                        rawJson,
                                        VoteBody.VoteResponse::class.java
                                    )
                                    val message = registerResponse.message
                                    Log.i("Upvote", "message: $message ")
                                    liveDataMessage.postValue("upvote")

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataMessage.postValue("-1")
                                    Log.e("ResponseError", "Error reading response body: ${e.message}")
                                }
                            } else {
                                liveDataMessage.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            liveDataMessage.postValue("-1")
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                Log.d("ResponseSuccess", "Body: $errorBody Response Code: $responseCode")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("onFailure", "Post Resource Request")
                    }
                }
            )
        }
    }

    /**
     * Down voting with respect to body
     */
    fun downvote(postRequest: VoteBody.VoteRequestBody) {
        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token", "Token $token")
        if (!token.isNullOrEmpty()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.DOWNVOTE,
                requestType = RequestType.PUT,
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
                                    val registerResponse = gson.fromJson(
                                        rawJson,
                                        VoteBody.VoteResponse::class.java
                                    )
                                    val message = registerResponse.message
                                    Log.i("Upvote", "message: $message ")
                                    liveDataMessage.postValue("downvote")

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataMessage.postValue("-1")
                                    Log.e("ResponseError", "Error reading response body: ${e.message}")
                                }
                            } else {
                                liveDataMessage.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            liveDataMessage.postValue("-1")
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                Log.d("ResponseSuccess", "Body: $errorBody Response Code: $responseCode")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("onFailure", "Post Resource Request")
                    }
                }
            )
        }
    }

    /**
     * Unvoting with respect to body
     */
    fun unvote(postRequest: VoteBody.VoteRequestBody) {
        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token", "Token $token")
        if (!token.isNullOrEmpty()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.UNVOTE,
                requestType = RequestType.PUT,
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
                                    val registerResponse = gson.fromJson(
                                        rawJson,
                                        VoteBody.VoteResponse::class.java
                                    )
                                    val message = registerResponse.message
                                    Log.i("Unvote", "message: $message ")
                                    liveDataMessage.postValue("unvote")

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataMessage.postValue("-1")
                                    Log.e("ResponseError", "Error reading response body: ${e.message}")
                                }
                            } else {
                                liveDataMessage.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            liveDataMessage.postValue("-1")
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                Log.d("ResponseSuccess", "Body: $errorBody Response Code: $responseCode")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("onFailure", "Post Resource Request")
                    }
                }
            )
        }
    }

    /**
     * Checking vote status
     */
    fun checkvote(entityType: String, entityID: String) {
        val queries = mutableMapOf<String, String>()
        queries["entityType"] = entityType
        queries["entityID"] = entityID

        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token", "Token $token")

        if (!token.isNullOrEmpty()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )
            networkManager.makeRequest(
                endpoint = Endpoint.CHECKVOTE,
                requestType = RequestType.GET,
                headers = headers,
                queries = queries,
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
                                    val voteResponse = gson.fromJson(rawJson, VoteBody.VoteResponse::class.java)
                                    val message = voteResponse.message
                                    Log.i("ResponseSuccess", "Given Vote: $message ")
                                    liveDataMessage.postValue(message)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataMessage.postValue("-1")
                                    Log.e("ResponseError", "Error reading response body: ${e.message}")
                                }
                            } else {
                                liveDataMessage.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            liveDataMessage.postValue("-1")
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                Log.d("ResponseSuccess", "Body: $errorBody Response Code: $responseCode")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("onFailure", "Happens")
                    }
                }
            )
        }
    }


}