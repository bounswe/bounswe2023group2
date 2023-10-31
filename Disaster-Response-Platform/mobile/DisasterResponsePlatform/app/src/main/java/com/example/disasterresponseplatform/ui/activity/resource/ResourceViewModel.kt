package com.example.disasterresponseplatform.ui.activity.resource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.data.database.resource.Resource
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.ResourceBody
import com.example.disasterresponseplatform.data.repositories.ResourceRepository
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.utils.DateUtil
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ResourceViewModel @Inject constructor(private val resourceRepository: ResourceRepository): ViewModel() {

    fun insertResource(resource: Resource){
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.insertResource(resource)
        }

    }

    fun getLocation(creatorName: String): String? = resourceRepository.getLocation(creatorName)

    fun getAllResources(): List<Resource>? = resourceRepository.getAllResources()

    private val networkManager = NetworkManager()

    private val liveDataResponse = MutableLiveData<ResourceBody.ResourceResponse>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<ResourceBody.ResourceResponse> = liveDataResponse

    fun createResourceList(resourceResponse: ResourceBody.ResourceResponse): List<Resource>{
        Log.d("createResourceList", "resourceResponse: $resourceResponse")
        val currentList = resourceResponse.resources
        Log.d("createResourceList", "currentList: $currentList")
        val lst = mutableListOf<Resource>()
        currentList.forEach{responseItem ->
            //Log.d("createResourceList", "responseItem: $responseItem")
            //Log.d("createResourceList", "responseItemDetails: ${responseItem.details}")
            val details = returnDetailsAsString(responseItem.details)
            val location = returnLocationAsString(responseItem.x, responseItem.y)
            val needType = returnNeedType(responseItem.type)
            val time = DateUtil.getDate("dd-MM-yy").toString()
            val id  = BigInteger(responseItem._id.uppercase(), 16).toLong()
            val currentResource = Resource(id,responseItem.created_by,responseItem.condition,responseItem.currentQuantity,needType,details,time,location)
            lst.add(currentResource)
        }
        return lst.toList()
    }

    private fun returnNeedType(type: String): NeedTypes{
        val needType: NeedTypes = when(type.lowercase()){
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

    private fun returnLocationAsString(x: Float?, y: Float?): String{
        var location = " "
        if (x != null){
            location += "x : $x "
        }
        if (y != null){
            location += "y: $y"
        }
        return location
    }
    private fun returnDetailsAsString(resourceDetails: ResourceBody.ResourceDetails): String{
        var detailsString = ""
        if (!resourceDetails.size.isNullOrEmpty()){
            detailsString += "size: ${resourceDetails.size} "
        }
        if (!resourceDetails.gender.isNullOrEmpty()){
            detailsString += "gender: ${resourceDetails.gender} "
        }
        if (resourceDetails.age != null){
            detailsString += "age: ${resourceDetails.age} "
        }
        if (!resourceDetails.subtype.isNullOrEmpty()){
            detailsString += "subtype: ${resourceDetails.subtype} "
        }
        if (!resourceDetails.expiration_date.isNullOrEmpty()){
            detailsString += "expiration_date: ${resourceDetails.expiration_date} "
        }
        if (!resourceDetails.allergens.isNullOrEmpty()){
            detailsString += "allergens: ${resourceDetails.allergens} "
        }
        if (!resourceDetails.disease_name.isNullOrEmpty()){
            detailsString += "disease_name: ${resourceDetails.disease_name} "
        }
        if (!resourceDetails.medicine_name.isNullOrEmpty()){
            detailsString += "medicine_name: ${resourceDetails.medicine_name} "
        }
        if (!resourceDetails.start_location.isNullOrEmpty()){
            detailsString += "start_location: ${resourceDetails.start_location} "
        }
        if (!resourceDetails.end_location.isNullOrEmpty()){
            detailsString += "end_location: ${resourceDetails.end_location} "
        }
        if (resourceDetails.number_of_people != null){
            detailsString += "number_of_people: ${resourceDetails.number_of_people} "
        }
        if (!resourceDetails.weather_condition.isNullOrEmpty()){
            detailsString += "weather_condition: ${resourceDetails.weather_condition} "
        }
        if (!resourceDetails.SKT.isNullOrEmpty()){
            detailsString += "SKT: ${resourceDetails.SKT} "
        }
        return detailsString
    }
    fun sendGetAllRequest(){
        val token = DiskStorageManager.getKeyValue("token")
        Log.i("token","Token $token")
        if (!token.isNullOrEmpty()){
            val headers = mapOf(
                // Not needed for Sign Up
                "Authorization" to token,
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
                        Log.d("ResponseInfo", "Status Code: ${response.code()}")
                        Log.d("ResponseInfo", "Headers: ${response.headers()}")

                        if (response.isSuccessful) {
                            val rawJson = response.body()?.string()
                            if (rawJson != null) {
                                try {
                                    Log.d("ResponseSuccess", "Body: $rawJson")
                                    val gson = Gson()
                                    val resourceResponse = gson.fromJson(rawJson, ResourceBody.ResourceResponse::class.java)
                                    if (resourceResponse != null){ // TODO check null
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

}