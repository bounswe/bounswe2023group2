package com.example.disasterresponseplatform.ui.activity.event

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.database.event.Event
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.EventBody
import com.example.disasterresponseplatform.data.repositories.EventRepository
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
class EventViewModel@Inject constructor(private val eventRepository: EventRepository) : ViewModel() {

    val networkManager = NetworkManager()

    private val liveDataResponse = MutableLiveData<EventBody.EventResponse>()

    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataResponse(): LiveData<EventBody.EventResponse> = liveDataResponse
    fun getAllEvents(queries: MutableMap<String, String>? = null){
        val headers = mapOf(
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.EVENT,
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
                                val eventResponse = gson.fromJson(rawJson, EventBody.EventResponse::class.java)
                                if (eventResponse != null) { // TODO check null
                                    Log.d("ResponseSuccess", "eventResponse: $eventResponse")
                                    liveDataResponse.postValue(eventResponse)
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

    fun sendGetSearchResult(query: String) {
        val token = DiskStorageManager.getKeyValue("token")
        val headers = mapOf(
            "Authorization" to "bearer $token",
            "Content-Type" to "application/json"
        )
        networkManager.makeRequest(
            endpoint = Endpoint.SEARCH_EVENT,
            requestType = RequestType.GET,
            headers = headers,
            id = query,
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
                                val eventResponse = gson.fromJson(rawJson, EventBody.EventSearchResponse::class.java)
                                if (eventResponse != null) { // TODO check null
                                    Log.d("ResponseSuccess", "eventResponse: $eventResponse")
                                    liveDataResponse.postValue(EventBody.EventResponse(eventResponse.results))
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

    private val liveDataEventID = MutableLiveData<String>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataEventID(): LiveData<String> = liveDataEventID

    /**
     * It POST or PUT event with respect to ID
     */
    fun postEvent(postRequest: EventBody.EventPostBody, id: String? = null){
        val token = DiskStorageManager.getKeyValue("token")
        if (DiskStorageManager.checkToken()) {
            val headers = mapOf(
                "Authorization" to "bearer $token",
                "Content-Type" to "application/json"
            )

            val gson = Gson()
            val json = gson.toJson(postRequest)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            Log.i("ResponseInfo","RequestBody: $requestBody")
            val requestType = if (id == null) RequestType.POST else RequestType.PATCH

            Log.d("requestBody", json.toString())
            networkManager.makeRequest(
                endpoint = Endpoint.EVENT,
                requestType = requestType,
                headers = headers,
                null,
                requestBody = requestBody,
                id, // event's ID
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
                                    val eventResponse = gson.fromJson(
                                        rawJson,
                                        EventBody.PostEventResponseList::class.java
                                    )
                                    val eventID = eventResponse.events[0]._id
                                    Log.i("Created Event ", "ID: $eventID ")
                                    val currentID = "" + eventID
                                    liveDataEventID.postValue(currentID)

                                } catch (e: IOException) {
                                    // Handle IOException if reading the response body fails
                                    liveDataEventID.postValue("-1")
                                    Log.e(
                                        "ResponseError",
                                        "Error reading response body: ${e.message}"
                                    )
                                }
                            } else {
                                liveDataEventID.postValue("-1")
                                Log.d("ResponseSuccess", "Body is null")
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            if (errorBody != null) {
                                val responseCode = response.code()
                                liveDataEventID.postValue("-1")
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
    }

    private val liveDataIsDeleted = MutableLiveData<Boolean>()
    // this is for updating LiveData, it can be observed from where it is called
    fun getLiveDataIsDeleted(): LiveData<Boolean> = liveDataIsDeleted

    /**
     * It deletes event respect to ID, if it deletes successful, it post a value into livedata to notify UI
     */
    fun deleteEvent(ID: String) {
        val headers = mapOf(
            "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
            "Content-Type" to "application/json"
        )
        val networkManager = NetworkManager()
        networkManager.makeRequest(
            endpoint = Endpoint.EVENT,
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
     * It inserts event in a background (IO) thread
     */
    fun insertEvent(event: Event){
        viewModelScope.launch(Dispatchers.IO){
            eventRepository.insertEvent(event)
        }
    }

    fun getAllEvents(): List<Event>?{
        return eventRepository.getAllEvents()
    }

    fun deleteEvent(id: Int){
        viewModelScope.launch(Dispatchers.IO){
            eventRepository.deleteEvent(id)
        }
    }

    /**
     * This functions get the local object and prepare it as to send to the backend
     */
    fun prepareBodyFromLocal(event: Event, activity: FragmentActivity): EventBody.EventPostBody {
        val type =
            when (event.type){
                "Enkaz" -> "Debris"
                "Altyapı" -> "Infrastructure"
                "Afet" -> "Disaster"
                "Yardım Noktası" -> "Help-Arrived"
                else -> event.type
            }
        val address = if (event.address.contains(activity.getString(R.string.selected_from_map))) "" else " address: " + event.address
        val additionalNotes = "No Internet Connection "+ event.additionalNotes + address
        val shortDescription = event.shortDescription
        val createdTime = "${DateUtil.getDate("yyyy-MM-dd")} ${DateUtil.getTime("HH:mm:ss")}"
        val x = event.x
        val y = event.y
        return EventBody.EventPostBody(type,null,true,x,y,null,null,
            createdTime,shortDescription,additionalNotes)
    }

}