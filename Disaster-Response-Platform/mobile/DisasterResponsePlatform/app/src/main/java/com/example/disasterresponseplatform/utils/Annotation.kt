package com.example.disasterresponseplatform.utils

import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.ResponseBody

class Annotation {

    private val publishNetworkManager = NetworkManager()
    private val getNetworkManager = NetworkManager("http://3.218.226.215:18000/api/")
    fun publishAnnotation(key: String, value: String) {
        publishNetworkManager.makeRequest(
            endpoint = Endpoint.SIMPLE_ANNOTATION,
            requestType = RequestType.POST,
            headers = mapOf("Content-Type" to "application/json"),
            queries = mapOf(
                "url" to "http://3.218.226.215:3000/annotation",
                "annotation_text" to value,
                "tags" to key
            ),
            callback = object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    println("Failed to publish annotation")
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    println("Published annotation")
                }
            }
        )
    }

    fun getAnnotations(key: String, callback: (input: String) -> Unit) {
        getNetworkManager.makeRequest(
            endpoint = Endpoint.ANNOTATIONS,
            requestType = RequestType.GET,
            headers = mapOf("Content-Type" to "application/json"),
            queries = mapOf(
                "url" to "http://3.218.226.215:3000/annotation",
                "hidden" to "false",
                "tag" to key
            ),
            callback = object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    println("Failed to fetch annotation")
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        var body = response.body()?.string()
                        if (body != null) {
                            body = body.substring(body.indexOf("\"value\": \"") + 10)
                            body = body.substring(0, body.indexOf("\\n\\n\""))
                            callback(body)
                        }
                    }
                }
            }
        )
    }
}