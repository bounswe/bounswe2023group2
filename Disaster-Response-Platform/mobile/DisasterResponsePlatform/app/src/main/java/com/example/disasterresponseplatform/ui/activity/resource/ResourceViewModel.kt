package com.example.disasterresponseplatform.ui.activity.resource

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.ResourceBody
import com.example.disasterresponseplatform.data.repositories.ResourceRepository
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(private val resourceRepository: ResourceRepository): ViewModel() {

    fun insertResource(resource: Resource) {
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.insertResource(resource)
        }
    }

    fun deleteResource(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.deleteResource(id)
        }
    }

    fun getAllResources(): List<Resource>? = resourceRepository.getAllResources()

    /**
     * This functions get the local object and prepare it as to send to the backend
     */
    fun prepareBodyFromLocal(resource: Resource,activity: FragmentActivity): ResourceBody.ResourceRequestBody {
        val quantity = resource.quantity
        val type = resource.type
        val additionalNotes = resource.additionalNotes
        val address = resource.address
        val shortDescription = resource.shortDescription
        val x = resource.x
        val y = resource.y
        //details
        val detailsMap = mutableMapOf<String, String>()
        // if address is given by hand
        if (!address.contains(activity.getString(R.string.selected_from_map))) detailsMap["address"] = address
        detailsMap["additionalNotes"] = additionalNotes
        detailsMap["subtype"] = "No Internet Connection"
        return ResourceBody.ResourceRequestBody(shortDescription,quantity,quantity,type,detailsMap,x,y,
            null,null,null,true,0,0)
    }

    private val networkManager = NetworkManager()

    private val liveDataResponse = MutableLiveData<ResourceBody.ResourceResponse>()

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<ResourceBody.ResourceResponse> = liveDataResponse

    fun sendGetAllRequest(
        queries: MutableMap<String, String>? = null
    ) {
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.RESOURCE,
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
                                val resourceResponse = gson.fromJson(rawJson, ResourceBody.ResourceResponse::class.java)
                                if (resourceResponse != null) {
                                    Log.d("ResponseSuccess", "resourceResponse: $resourceResponse")
                                    liveDataResponse.postValue(resourceResponse)
                                }
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
                            var responseCode = response.code()
                            Log.d("ResponseSuccess", "Error Body: $errorBody Response Code: $responseCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("onFailure", "Happens")
                }
            }
        )
    }

    private val liveDataResourceID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResourceID(): LiveData<String> = liveDataResourceID

    /**
     * It send POST or PUT request with respect to id, if there was an id it should be PUT
     */
    fun postResourceRequest(postRequest: ResourceBody.ResourceRequestBody, id: String? = null) {
        val token = DiskStorageManager.getKeyValue("token")
        if (DiskStorageManager.checkToken()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val requestType = if (id == null) RequestType.POST else RequestType.PUT

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.RESOURCE,
                requestType = requestType,
                headers = headers,
                requestBody = requestBody,
                id = id, // Resource's ID
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
                                        ResourceBody.PostRegisterResponseList::class.java
                                    )
                                    val resourceID = registerResponse.resources[0]._id
                                    Log.i("Created Resource ", "ID: $resourceID ")
                                    liveDataResourceID.postValue(resourceID)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataResourceID.postValue("-1")
                                    Log.e(
                                        "ResponseError",
                                        "Error reading response body: ${e.message}"
                                    )
                                }
                            } else {
                                liveDataResourceID.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            liveDataResourceID.postValue("-1")
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                var responseCode = response.code()
                                Log.d(
                                    "ResponseSuccess",
                                    "Body: $errorBody Response Code: $responseCode"
                                )
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


    private val liveDataIsDeleted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataIsDeleted(): LiveData<Boolean> = liveDataIsDeleted

    /**
     * It deletes resource with respect to ID, if it deletes successful, it post a value into livedata to notify UI
     */
    fun deleteResource(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val networkManager = NetworkManager()
        networkManager.makeRequest(
            endpoint = Endpoint.RESOURCE,
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