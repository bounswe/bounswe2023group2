package com.example.disasterresponseplatform.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.ActionBody
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.osmdroid.api.IGeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

data class MapState(val zoomLevel: Int, val centerPoint: IGeoPoint)
class MapViewModel@Inject constructor() : ViewModel() {

    private val networkManager = NetworkManager()

    private val liveDataNeedResponse = MutableLiveData<NeedBody.NeedResponse>()
    private val liveDataResourceResponse = MutableLiveData<ResourceBody.ResourceResponse>()
    private val liveDataActionResponse = MutableLiveData<ActionBody.ActionsResponse>()
    private var savedMapState: MapState? = null

    fun saveMapState(zoomLevel: Int, center: IGeoPoint) {
        savedMapState = MapState(zoomLevel, center)
    }

    fun getMapState(): MapState? {
        return savedMapState
    }

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataNeedResponse(): LiveData<NeedBody.NeedResponse> = liveDataNeedResponse

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResourceResponse(): LiveData<ResourceBody.ResourceResponse> = liveDataResourceResponse

    fun getLiveDataActionsResponse(): LiveData<ActionBody.ActionsResponse> = liveDataActionResponse

    fun sendGetAllActionsRequest() {
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.ACTIONS,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseAction", "Status Code: ${response.code()}")
                    Log.d("ResponseAction", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val actionResponse = gson.fromJson(
                                    rawJson,
                                    ActionBody.ActionsResponse::class.java
                                )
                                if (actionResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "actionResponse: $actionResponse"
                                    )
                                    liveDataActionResponse.postValue(actionResponse)
                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseResourceFail", "Body: $errorBody")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("onFailure", "Happens")
                }
            }
        )
    }
    fun sendGetAllResourceRequest() {
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.RESOURCE,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfoRequest", "Status Code: ${response.code()}")
                    Log.d("ResponseInfoRequest", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val resourceResponse = gson.fromJson(
                                    rawJson,
                                    ResourceBody.ResourceResponse::class.java
                                )
                                if (resourceResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "resourceResponse: $resourceResponse"
                                    )
                                    liveDataResourceResponse.postValue(resourceResponse)
                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseResourceFail", "Body: $errorBody")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("onFailure", "Happens")
                }
            }
        )
    }

    fun sendGetAllNeedRequest() {
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.NEED,
            requestType = RequestType.GET,
            headers = headers,
            callback = object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("ResponseInfoNeed", "Status Code: ${response.code()}")
                    Log.d("ResponseInfoNeed", "Headers: ${response.headers()}")

                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        if (rawJson != null) {
                            try {
                                Log.d("ResponseSuccess", "Body: $rawJson")
                                val gson = Gson()
                                val needResponse = gson.fromJson(
                                    rawJson,
                                    NeedBody.NeedResponse::class.java
                                )
                                if (needResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "needResponse: $needResponse"
                                    )
                                    liveDataNeedResponse.postValue(needResponse)
                                }
                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            var responseCode = response.code()
                            Log.d("ResponseSuccess", "Body: $errorBody")
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