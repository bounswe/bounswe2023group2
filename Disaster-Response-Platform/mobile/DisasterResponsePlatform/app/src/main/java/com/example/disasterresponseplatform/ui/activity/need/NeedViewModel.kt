package com.example.disasterresponseplatform.ui.activity.need

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.repositories.NeedRepository
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.utils.DateUtil
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

    fun getX(creatorID: String): Double?{
        return needRepository.getX(creatorID)
    }

    fun getY(creatorID: String): Double?{
        return needRepository.getY(creatorID)
    }

    fun getAllNeeds(): List<Need>? = needRepository.getAllNeeds()

    private val networkManager = NetworkManager()

    private val liveDataResponse = MutableLiveData<NeedBody.NeedResponse>()

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<NeedBody.NeedResponse> = liveDataResponse

    fun createNeedList(needResponse: NeedBody.NeedResponse): List<Need> {
        Log.d("createNeedList", "needResponse: $needResponse")
        val currentList = needResponse.needs
        Log.d("createNeedList", "currentList: $currentList")
        val lst = mutableListOf<Need>()
        currentList.forEach { responseItem ->
            //Log.d("createNeedList", "responseItem: $responseItem")
            //Log.d("createNeedList", "responseItemDetails: ${responseItem.details}")
            val details = responseItem.returnDetailsAsString()
            val needType = responseItem.returnNeedType()
            val time = DateUtil.getDate("dd-MM-yy").toString()
            val coordinateX = if (responseItem.x == null) 1.0 else responseItem.x.toDouble()
            val coordinateY = if (responseItem.y == null) 1.0 else responseItem.y.toDouble()
            val currentNeed = Need(
                responseItem._id,
                responseItem.created_by,
                needType,
                details,
                time,
                responseItem.initialQuantity,
                coordinateX,
                coordinateY,
                responseItem.urgency
            )
            lst.add(currentNeed)
        }
        return lst.toList()
    }

    fun sendGetAllRequest() {
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
                                    NeedBody.NeedResponse::class.java
                                )
                                if (needResponse != null) { // TODO check null
                                    Log.d(
                                        "ResponseSuccess",
                                        "needResponse: $needResponse"
                                    )
                                    liveDataResponse.postValue(needResponse)
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

    private val liveDataNeedID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataNeedID(): LiveData<String> = liveDataNeedID

    /**
     * It send POST or PUT request with respect to id, if there was an id it should be PUT
     */
    fun postNeedRequest(postRequest: Need, id: String? = null) {
        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token", "Token $token")
        if (!token.isNullOrEmpty()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val type: String = when (postRequest.type) {
                NeedTypes.Clothes -> "Clothes"
                NeedTypes.Food -> "Food"
                NeedTypes.Shelter -> "Shelter"
                NeedTypes.Medication -> "Medication"
                NeedTypes.Transportation -> "Transportation"
                NeedTypes.Tools -> "Tools"
                NeedTypes.Human -> "Human"
                else -> "Other"
            }


            val needRequestBody = NeedBody.NeedRequestBody(
                created_by = postRequest.creatorName,
                initialQuantity = postRequest.quantity,
                urgency = postRequest.urgency ?: 0,
                unsuppliedQuantity = postRequest.quantity,
                type = type,
                details = NeedBody.Details(postRequest.details),
                x = postRequest.coordinateX,
                y = postRequest.coordinateY
            )
            val gson = Gson()
            val json = gson.toJson(needRequestBody)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val requestType = if (id == null) RequestType.POST else RequestType.PUT

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.NEED,
                requestType = requestType,
                headers = headers,
                requestBody = requestBody,
                id, // need's ID
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

}