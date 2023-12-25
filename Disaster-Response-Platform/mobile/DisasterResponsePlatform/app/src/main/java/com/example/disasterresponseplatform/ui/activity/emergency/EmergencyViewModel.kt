package com.example.disasterresponseplatform.ui.activity.emergency

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.emergency.Emergency
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.EmergencyBody
import com.example.disasterresponseplatform.data.repositories.EmergencyRepository
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
class EmergencyViewModel@Inject constructor(private val emergencyRepository: EmergencyRepository) : ViewModel() {

    /**
     * It inserts emergency in a background (IO) thread
     */
    fun insertEmergency(emergency: Emergency){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.insertEmergency(emergency)
        }
    }

    fun getAllEmergencies(): List<Emergency>? = emergencyRepository.getAllEmergencies()

    fun deleteEmergency(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            emergencyRepository.deleteEmergency(id)
        }
    }

    val networkManager = NetworkManager()

    fun getAllEmergencies(queries: MutableMap<String, String>? = null){
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.EMERGENCY,
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
                                val emergencyResponse = gson.fromJson(rawJson, EmergencyBody.EmergencyResponse::class.java)
                                if (emergencyResponse != null) { // TODO check null
                                    Log.d("ResponseSuccess", "emergencyResponse: $emergencyResponse")
                                    liveDataResponse.postValue(emergencyResponse)
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


    /**
     * It POST or PUT emergency with respect to ID
     */
    fun postEmergency(postRequest: EmergencyBody.EmergencyPostBody, id: String? = null){
        val token = DiskStorageManager.getKeyValue("token")
        var headers = mapOf("Content-Type" to "application/json")
        var ep = Endpoint.EMERGENCY_ANONYMOUS

        if (DiskStorageManager.checkToken()) {
            headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )
            ep = Endpoint.EMERGENCY
        }

        val gson = Gson()
        val json = gson.toJson(postRequest)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.i("ResponseInfo","RequestBody: $requestBody")
        val requestType = if (id == null) RequestType.POST else RequestType.PATCH

        Log.d("requestBody", json.toString())
        networkManager.makeRequest(
            endpoint = ep,
            requestType = requestType,
            headers = headers,
            null,
            requestBody = requestBody,
            id, // emergency's ID
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
                                val emergencyResponse = gson.fromJson(
                                    rawJson,
                                    EmergencyBody.PostEmergencyResponseList::class.java
                                )
                                val emergencyID = emergencyResponse.emergencies[0]._id
                                Log.i("Created Emergency ", "ID: $emergencyID ")
                                val currentID = "" + emergencyID
                                liveDataEmergencyID.postValue(currentID)

                            } catch (e: IOException) {
                                // Handle IOException if reading the response body fails
                                liveDataEmergencyID.postValue("-1")
                                Log.e(
                                    "ResponseError",
                                    "Error reading response body: ${e.message}"
                                )
                            }
                        } else {
                            liveDataEmergencyID.postValue("-1")
                            Log.d("ResponseSuccess", "Body is null")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            val responseCode = response.code()
                            liveDataEmergencyID.postValue("-1")
                            Log.d(
                                "ResponseSuccess",
                                "Error Body: $errorBody Response Code: $responseCode"
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


    /**
     * It deletes emergency respect to ID, if it deletes successful, it post a value into livedata to notify UI
     */
    fun deleteEmergency(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val networkManager = NetworkManager()
        networkManager.makeRequest(
            endpoint = Endpoint.EMERGENCY,
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
                            Log.d("Error Body:", errorBody)
                        }
                    }
                }
            })
    }




    /**
     * This functions get the local object and prepare it as to send to the backend
     */
    fun prepareBodyFromLocal(emergency: Emergency, activity: FragmentActivity): EmergencyBody.EmergencyPostBody {
        return EmergencyBody.EmergencyPostBody(
            contact_name = emergency.contactName,
            contact_number = emergency.contactNumber,
            created_at = "${DateUtil.getDate("yyyy-MM-dd")} ${DateUtil.getTime("HH:mm:ss")}",
            emergency_type = emergency.type,
            description = emergency.description,
            x = emergency.x,
            y = emergency.y,
            location = emergency.location,
            is_active = true,
            )
    }


    private val liveDataIsDeleted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataIsDeleted(): LiveData<Boolean> = liveDataIsDeleted


    private val liveDataResponse = MutableLiveData<EmergencyBody.EmergencyResponse>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<EmergencyBody.EmergencyResponse> = liveDataResponse

    private val liveDataEmergencyID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataEmergencyID(): LiveData<String> = liveDataEmergencyID


}