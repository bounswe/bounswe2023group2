package com.example.disasterresponseplatform.utils

import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object UserRoleUtil {
    val userRoleMap: MutableMap<String, String?> = mutableMapOf()
    val headers = mapOf(
        "Authorization" to "bearer ${DiskStorageManager.getKeyValue("token")}",
        "Content-Type" to "application/json"
    )
    val networkManager = NetworkManager()

    fun isCredible(username: String): Boolean {
        if (username !in userRoleMap) {
            val url = "http://3.218.226.215:8000/api/users/$username"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "bearer ${DiskStorageManager.getKeyValue("token")}")
                .addHeader("Content-Type", "application/json")
                .get()
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    userRoleMap[username] = null
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.body == null) {
                        userRoleMap[username] = null
                    } else {
                        val body = response.body!!.string()
                        println("Userrole response body: $body")
                        if (body.contains("CREDIBLE")) {
                            userRoleMap[username] = "CREDIBLE"
                        } else if (body.contains("GUEST")) {
                            userRoleMap[username] = "GUEST"
                        } else if (body.contains("AUTHENTICATED")) {
                            userRoleMap[username] = "AUTHENTICATED"
                        } else if (body.contains("ADMIN")) {
                            userRoleMap[username] = "ADMIN"
                        } else if (body.contains("ROLE_BASED")) {
                            userRoleMap[username] = "ROLE_BASED"
                        } else userRoleMap[username] = null
                    }
                }
            })
        }
        while (username !in userRoleMap) {
            println("Waiting for userRole...")
            runBlocking {
                delay(10)
            }
        }
        return userRoleMap[username] == "CREDIBLE"
    }

    fun isCredibleNonBlocking(username: String, result: (isCredible: Boolean) -> Unit) {
        if (username !in userRoleMap) {
            val url = "http://3.218.226.215:8000/api/users/$username"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "bearer ${DiskStorageManager.getKeyValue("token")}")
                .addHeader("Content-Type", "application/json")
                .get()
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    userRoleMap[username] = null
                    result(false)
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    if (response.body == null) {
                        userRoleMap[username] = null
                    } else {
                        val body = response.body!!.string()
                        println("Userrole response body: $body")
                        if (body.contains("CREDIBLE")) {
                            userRoleMap[username] = "CREDIBLE"
                        } else if (body.contains("GUEST")) {
                            userRoleMap[username] = "GUEST"
                        } else if (body.contains("AUTHENTICATED")) {
                            userRoleMap[username] = "AUTHENTICATED"
                        } else if (body.contains("ADMIN")) {
                            userRoleMap[username] = "ADMIN"
                        } else if (body.contains("ROLE_BASED")) {
                            userRoleMap[username] = "ROLE_BASED"
                        } else userRoleMap[username] = null
                        result(userRoleMap[username] == "CREDIBLE")
                    }
                }
            })
        } else {
            result(userRoleMap[username] == "CREDIBLE")
        }
    }
}