package com.example.disasterresponseplatform.utils

import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.IOException

class Annotation {

    private val publishNetworkManager = NetworkManager()
    private val getNetworkManager = NetworkManager("http://3.218.226.215:18000/api/")
    fun publishAnnotation(key: String, value: String) {
        println("publishing annotation: $key, $value")
        val url = "http://3.218.226.215:8000/api/simple_annotation?url=http%3A%2F%2F3.218.226.215%3A3000%2Fannotation&annotation_text=$value&tags=$key"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post("{}".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Failed to publish annotation")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    println("Annotation published")
                } else {
                    println("Published annotation")
                    println(response.message)
                    println(response.body?.string())
                }
            }
        })
//        publishNetworkManager.makeRequest(
//            endpoint = Endpoint.SIMPLE_ANNOTATION,
//            requestType = RequestType.POST,
//            headers = mapOf("Content-Type" to "application/json"),
//            queries = mapOf(
//                "url" to "http://3.218.226.215:3000/annotation",
//                "annotation_text" to value,
//                "tags" to key
//            ),
//            callback = object : retrofit2.Callback<ResponseBody> {
//                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
//                    println("Failed to publish annotation")
//                }
//
//                override fun onResponse(
//                    call: retrofit2.Call<ResponseBody>,
//                    response: retrofit2.Response<ResponseBody>
//                ) {
//                    if (response.isSuccessful) {
//                        println("Annotation published")
//                    } else {
//                        println("Published annotation")
//                        println(response.code())
//                        println(response.body()?.string())
//                    }
//                }
//            }
//        )
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
                            try {
                                body = body.substring(body.indexOf("\"value\":\"") + 9)
                                body = body.substring(0, body.indexOf("\\n\\n\""))
                                callback(body)
                            } catch (e: Exception) {
                                println("Failed to parse annotation")
                            }
                        }
                    }
                }
            }
        )
    }
}