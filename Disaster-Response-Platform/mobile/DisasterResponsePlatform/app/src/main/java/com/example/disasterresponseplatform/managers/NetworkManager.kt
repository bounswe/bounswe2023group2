package com.example.disasterresponseplatform.managers

import com.example.disasterresponseplatform.models.enums.Endpoint
import com.example.disasterresponseplatform.models.enums.RequestType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Manages network operations for the application.
 * <p>
 * This class provides methods to make network requests using Retrofit. It supports various HTTP methods including GET, POST, PUT, and DELETE.
 * </p>
 * <p>
 * Usage example:
 * </p>
 * <pre>
 * NetworkManager networkManager = new NetworkManager();
 * networkManager.makeRequest(
 *     endpoint = Endpoint.DATA,
 *     requestType = RequestType.GET,
 *     headers = headersMap,
 *     callback = new retrofit2.Callback<DataResponse>() {
 *         // Handle responses and failures
 *     }
 * );
 * </pre>
 */
class NetworkManager {

    private val baseUrl = "https://v2.jokeapi.dev/"

    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
    /**
     * Makes a network request based on the provided parameters.
     *
     * @param endpoint The endpoint for the request, based on the Endpoint enum.
     * @param requestType The type of the request (GET, POST, PUT, DELETE).
     * @param headers A map containing header key-value pairs.
     * @param requestBody (Optional) The body for POST and PUT requests.
     * @param callback A Retrofit callback to handle the response or failure.
     * @param <T> The type of the response object.
     */
    fun <T> makeRequest(
        endpoint: Endpoint,
        requestType: RequestType,
        headers: Map<String, String>,
        requestBody: Any? = null,
        callback: retrofit2.Callback<T>
    ) {
        when (endpoint) {
            Endpoint.DATA -> {
                when (requestType) {
                    RequestType.GET -> {
                        val call = api.getData(endpoint.path, headers)
                        call.enqueue(callback as Callback<DataResponse>)
                    }
                    RequestType.POST -> {
                        val call = api.postData(endpoint.path, headers, requestBody!!)
                        call.enqueue(callback as Callback<DataResponse>)
                    }
                    RequestType.PUT -> {
                        val call = api.putData(endpoint.path, headers, requestBody!!)
                        call.enqueue(callback as Callback<DataResponse>)
                    }
                    RequestType.DELETE -> {
                        val call = api.deleteData(endpoint.path, headers)
                        call.enqueue(callback as Callback<DataResponse>)
                    }
                }
            }
            Endpoint.USER -> TODO()
            Endpoint.PRODUCTS -> TODO()
        }
    }

}
interface ApiService {

    @GET("{endpoint}")
    fun getData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>
    ): Call<DataResponse>

    @POST("{endpoint}")
    fun <T> postData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: T
    ): Call<DataResponse>

    @PUT("{endpoint}")
    fun <T> putData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: T
    ): Call<DataResponse>

    @DELETE("{endpoint}")
    fun deleteData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>
    ): Call<DataResponse>
}

data class DataResponse(val data: List<String>)
data class DataRequest(val key: String, val value: String)