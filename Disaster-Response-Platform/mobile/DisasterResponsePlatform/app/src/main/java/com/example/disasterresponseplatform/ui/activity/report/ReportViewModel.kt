package com.example.disasterresponseplatform.ui.activity.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.ReportBody
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel {
    val networkManager = NetworkManager()

    private var _reportValidation = MutableLiveData<String>()
    val reportValidation: LiveData<String> get() = _reportValidation

    fun sendReport(id: String, type: String, description: String) {
        val token = DiskStorageManager.getKeyValue("token")
        if (token.isNullOrEmpty()) {
            return
        }
        val headers = mapOf(
            "Authorization" to "bearer $token",
            "Content-Type" to "application/json"
        )
        val simpleMap= mutableMapOf<String,Any>()
        simpleMap["abc"] = 2015
        val reportRequestBody = ReportBody.ReportRequestBody(createdBy = token, description = description, reportType = type,id,simpleMap)
        val gson = Gson()
        val json = gson.toJson(reportRequestBody)
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.i("ResponseInfo","RequestBody: $requestBody")
        networkManager.makeRequest(
            endpoint = Endpoint.REPORTS,
            requestType = RequestType.POST,
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
                        _reportValidation.value = "Your report has been sent"
                    } else {
                        _reportValidation.value = "You report couldn't arrive"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("onFailure", "Post Action Request")
                }
            })
    }
}