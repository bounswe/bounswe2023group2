package com.example.disasterresponseplatform.ui.activity.need

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.NeedBody
import com.example.disasterresponseplatform.data.models.ResourceBody
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

    fun updateNeed(need: Need){
        viewModelScope.launch(Dispatchers.IO){
            needRepository.updateNeed(need)
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
        Log.d("createNeedList", "resourceResponse: $needResponse")
        val currentList = needResponse.needs
        Log.d("createResourceList", "currentList: $currentList")
        val lst = mutableListOf<Need>()
        currentList.forEach { responseItem ->
            //Log.d("createResourceList", "responseItem: $responseItem")
            //Log.d("createResourceList", "responseItemDetails: ${responseItem.details}")
            val details = returnDetailsAsString(responseItem.details)
            val needType = returnNeedType(responseItem.type)
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

    private fun returnNeedType(type: String): NeedTypes {
        val needType: NeedTypes = when (type.lowercase()) {
            "cloth" -> NeedTypes.Clothes
            "clothes" -> NeedTypes.Clothes
            "food" -> NeedTypes.Food
            "shelter" -> NeedTypes.Shelter
            "medication" -> NeedTypes.Medication
            "transportation" -> NeedTypes.Transportation
            "tools" -> NeedTypes.Tools
            "human" -> NeedTypes.Human
            else -> NeedTypes.Other
        }
        return needType
    }

    private fun returnDetailsAsString(resourceDetails: NeedBody.NeedDetails): String {
        var detailsString = ""
        if (!resourceDetails.size.isNullOrEmpty()) {
            detailsString += "size: ${resourceDetails.size} "
        }
        if (!resourceDetails.gender.isNullOrEmpty()) {
            detailsString += "gender: ${resourceDetails.gender} "
        }
        if (resourceDetails.age != null) {
            detailsString += "age: ${resourceDetails.age} "
        }
        if (!resourceDetails.subtype.isNullOrEmpty()) {
            detailsString += "subtype: ${resourceDetails.subtype} "
        }
        if (!resourceDetails.expiration_date.isNullOrEmpty()) {
            detailsString += "expiration_date: ${resourceDetails.expiration_date} "
        }
        if (!resourceDetails.allergens.isNullOrEmpty()) {
            detailsString += "allergens: ${resourceDetails.allergens} "
        }
        if (!resourceDetails.disease_name.isNullOrEmpty()) {
            detailsString += "disease_name: ${resourceDetails.disease_name} "
        }
        if (!resourceDetails.medicine_name.isNullOrEmpty()) {
            detailsString += "medicine_name: ${resourceDetails.medicine_name} "
        }
        if (!resourceDetails.start_location.isNullOrEmpty()) {
            detailsString += "start_location: ${resourceDetails.start_location} "
        }
        if (!resourceDetails.end_location.isNullOrEmpty()) {
            detailsString += "end_location: ${resourceDetails.end_location} "
        }
        if (resourceDetails.number_of_people != null) {
            detailsString += "number_of_people: ${resourceDetails.number_of_people} "
        }
        if (!resourceDetails.weather_condition.isNullOrEmpty()) {
            detailsString += "weather_condition: ${resourceDetails.weather_condition} "
        }
        if (!resourceDetails.SKT.isNullOrEmpty()) {
            detailsString += "SKT: ${resourceDetails.SKT} "
        }
        return detailsString
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
                                        "resourceResponse: $needResponse"
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

    private val liveDataResourceID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResourceID(): LiveData<String> = liveDataResourceID

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
                                    Log.i("Created Resource ", "ID: $needID ")
                                    val currentID = "" + needID
                                    liveDataResourceID.postValue(currentID)

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
                                val responseCode = response.code()
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

}