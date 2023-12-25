package com.example.disasterresponseplatform.ui.activity.need

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.repositories.NeedRepository
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
class NeedViewModel@Inject constructor(private val needRepository: NeedRepository) : ViewModel() {

    /**
     * It inserts need in a background (IO) thread
     */
    fun insertNeed(need: Need){
        viewModelScope.launch(Dispatchers.IO){
            needRepository.insertNeed(need)
        }
    }

    fun getAllNeeds(): List<Need>? = needRepository.getAllNeeds()

    fun deleteNeed(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            needRepository.deleteNeed(id)
        }
    }

    /**
     * This functions get the local object and prepare it as to send to the backend
     */
    fun prepareBodyFromLocal(need: Need,activity: FragmentActivity): NeedBody.NeedRequestBody {
        val quantity = need.quantity
        val type = need.type
        val additionalNotes = need.additionalNotes
        val address = need.address
        val shortDescription = need.shortDescription
        val x = need.x
        val y = need.y
        //details
        val detailsMap = mutableMapOf<String, String>()
        val postAddress: String? = if (!address.contains(activity.getString(R.string.selected_from_map))) address else null
        detailsMap["additionalNotes"] = additionalNotes
        detailsMap["subtype"] = "No Internet Connection"
        return NeedBody.NeedRequestBody(
            shortDescription, quantity, 1, quantity, type, detailsMap,postAddress,
            x, y, null, null, null, true, 0, 0
        )
    }


    private val networkManager = NetworkManager()

    private val liveDataResponse = MutableLiveData<NeedBody.NeedResponse>()

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<NeedBody.NeedResponse> = liveDataResponse

    fun sendGetAllRequest(
        queries: MutableMap<String, String>? = null
    ) {

        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.NEED,
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
                                val needResponse = gson.fromJson(rawJson, NeedBody.NeedResponse::class.java)
                                if (needResponse != null) { // TODO check null
                                    Log.d("ResponseSuccess", "needResponse: $needResponse")
                                    liveDataResponse.postValue(needResponse)
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
                            val responseCode = response.code()
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

    private val liveDataNeedID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataNeedID(): LiveData<String> = liveDataNeedID

    /**
     * It send POST or PUT request with respect to id, if there was an id it should be PUT
     */
    fun postNeedRequest(postRequest: NeedBody.NeedRequestBody, id: String? = null) {
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
                endpoint = Endpoint.NEED,
                requestType = requestType,
                headers = headers,
                requestBody = requestBody,
                id = id, // need's ID
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
                                    val needResponse = gson.fromJson(
                                        rawJson,
                                        NeedBody.PostNeedResponseList::class.java
                                    )
                                    val needID = needResponse.needs[0]._id
                                    Log.i("Created Need ", "ID: $needID ")
                                    val currentID = "" + needID
                                    liveDataNeedID.postValue(currentID)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataNeedID.postValue("-1")
                                    Log.e(
                                        "ResponseError",
                                        "Error reading response body: ${e.message}"
                                    )
                                }
                            } else {
                                liveDataNeedID.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                liveDataNeedID.postValue("-1")
                                Log.d(
                                    "ResponseSuccess",
                                    "Body: $errorBody Response Code: $responseCode"
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