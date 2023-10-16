package com.example.disasterresponseplatform.managers

import com.example.disasterresponseplatform.models.enums.Endpoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

class NetworkManager {

    private val baseUrl = "https://api.publicapis.org/entries/"

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    fun <T> makeRequest(endpoint: Endpoint, headers: Map<String, String>, requestBody: Any? = null, callback: retrofit2.Callback<T>) {
        val fullUrl = baseUrl + endpoint.name
        when (endpoint) {
            Endpoint.DATA-> {
                if (requestBody == null) {
                    val call = api.getData(fullUrl, headers)
                    call.enqueue(callback as Callback<DataResponse>)
                } else {
                    val call = api.postData(fullUrl, headers, requestBody as DataRequest)
                    call.enqueue(callback as Callback<DataResponse>)
                }
            }
            Endpoint.USER -> TODO()
            Endpoint.PRODUCTS -> TODO()
        }
    }

}
interface ApiService {

    @GET
    fun getData(@Url url: String, @HeaderMap headers: Map<String, String>): Call<DataResponse>

    @POST
    fun postData(@Url url: String, @HeaderMap headers: Map<String, String>, @Body requestBody: DataRequest): Call<DataResponse>

    // Add other endpoints as needed
}

data class DataResponse(val data: List<String>)
data class DataRequest(val key: String, val value: String)