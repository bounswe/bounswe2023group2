package com.example.disasterresponseplatform.managers

import android.util.Log
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap


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

    private val baseUrl = "http://3.218.226.215:8000/api/"

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
    fun makeRequest(
        endpoint: Endpoint,
        requestType: RequestType,
        headers: Map<String, String>,
        queries: Map<String, String>? = null,
        requestBody: RequestBody? = null,
        id: String? = null, // If it is not empty, send request endpoint.path + /id -> send id as /<id> for now
        callback: Callback<ResponseBody>,
    ) {
        when (endpoint) {
            Endpoint.DATA -> {
                when (requestType) {
                    RequestType.GET -> {
                        Log.d("RESPONSE", callback.toString())
                        val call = api.getData(endpoint.path, headers)
                        call.enqueue(callback)
                    }
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.postData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.PUT -> {
                        requestBody?.let { api.putData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.DELETE -> {
                        val call = api.deleteData(endpoint.path, headers)
                        call.enqueue(callback)
                    }
                    else -> {}
                }
            }
            Endpoint.USER -> TODO()
            Endpoint.PRODUCTS -> TODO()
            Endpoint.LOGIN -> {
                when (requestType) {
                    RequestType.GET -> {
                        val call = api.getData(endpoint.path, headers)
                        call.enqueue(callback)
                    }
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.postData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    else -> {}
                }
            }

            Endpoint.SIGNUP -> {
                when (requestType) {
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.postData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    else -> {}
                }
            }
            Endpoint.RESOURCE -> {
                when (requestType) {
                    RequestType.GET -> {
                        Log.d("RESPONSE", callback.toString())
                        if (queries.isNullOrEmpty()) {
                            Log.d("RESPONSE", callback.toString())
                            val call = api.getData(endpoint.path, headers)
                            call.enqueue(callback)
                        } else {
                            Log.d("RESPONSE", callback.toString())
                            val call = api.getQueryData(endpoint.path, headers, queries)
                            call.enqueue(callback)
                        }
                    }
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.postData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.PUT -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.putData(endpoint.path+id, headers, it) } // TODO endpoint.path, resource ID içermeli şimdilik dummy çözüm
                            ?.enqueue(callback)
                    }
                    RequestType.DELETE -> {
                        val call = api.deleteData(endpoint.path+"/"+id, headers)
                        call.enqueue(callback)
                    }
                    else -> {}
                }

            }
            Endpoint.NEED -> {
                when (requestType) {
                    RequestType.GET -> {
                        Log.d("RESPONSE", callback.toString())
                        if (queries.isNullOrEmpty()) {
                            Log.d("RESPONSE", callback.toString())
                            val call = api.getData(endpoint.path, headers)
                            call.enqueue(callback)
                        } else {
                            Log.d("RESPONSE", callback.toString())
                            val call = api.getQueryData(endpoint.path, headers, queries)
                            call.enqueue(callback)
                        }
                    }
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.postData(endpoint.path, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.PUT -> {
                        Log.d("RESPONSE", callback.toString())
                        requestBody?.let { api.putData(endpoint.path+id, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.DELETE -> {
                        val call = api.deleteData(endpoint.path+"/"+id, headers)
                        call.enqueue(callback)
                    }
                    else -> {}
                }
            }
            Endpoint.FORM_FIELDS_TYPE -> {
                Log.d("RESPONSE", callback.toString())
                id?.let {
                    val call = api.getData("${endpoint.path}/$id", headers)
                    call.enqueue(callback)
                }
            }
            Endpoint.GETUSER -> {
                Log.d("RESPONSE", callback.toString())
                id?.let {
                    val call = api.getData("${endpoint.path}/$id", headers)
                    call.enqueue(callback)
                }
            }
            else -> {
                when (requestType) {
                    RequestType.GET -> {
                        Log.d("RESPONSE", callback.toString())
                        var endpoint = endpoint.path
                        if (id != null) endpoint += "/$id"
                        val call = api.getData(endpoint, headers)
                        call.enqueue(callback)
                    }
                    RequestType.POST -> {
                        Log.d("RESPONSE", callback.toString())
                        var call = endpoint.path
                        if (id != null) call += "/$id"
                        requestBody?.let { api.postData(call, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.PUT -> {
                        var call = endpoint.path
                        if (id != null) call += "/$id"
                        requestBody?.let { api.putData(call, headers, it) }
                            ?.enqueue(callback)
                    }
                    RequestType.DELETE -> {
                        var call = endpoint.path
                        if (id != null) call += "/$id"
                        Log.i("Delete Call:" , call)
                        val callb = api.deleteData(call, headers)
                        callb.enqueue(callback)
                    }
                    RequestType.PATCH -> {
                        var call = endpoint.path
                        if (id != null) call += "/$id"
                        requestBody?.let { api.patchData(call, headers, it) }
                            ?.enqueue(callback)
                    }
                }
            }
        }
    }
}
interface ApiService {

    @GET("{endpoint}")
    fun getData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
    ): Call<ResponseBody>

    @GET("{endpoint}")
    fun getQueryData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @QueryMap queries: Map<String, String>?
    ): Call<ResponseBody>

    @POST("{endpoint}")
    fun postData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody,
    ): Call<ResponseBody>

    @PUT("{endpoint}")
    fun putData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody,
    ): Call<ResponseBody>

    @DELETE("{endpoint}")
    fun deleteData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
    ): Call<ResponseBody>

    @PATCH("{endpoint}")
    fun patchData(
        @Path("endpoint") endpoint: String,
        @HeaderMap headers: Map<String, String>,
        @Body requestBody: RequestBody
    ): Call<ResponseBody>

}
