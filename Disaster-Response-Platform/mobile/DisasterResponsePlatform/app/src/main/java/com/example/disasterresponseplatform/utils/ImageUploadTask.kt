package com.example.disasterresponseplatform.utils

import android.graphics.Bitmap
import android.os.AsyncTask
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class ImageUploadTask(private val bitmap: Bitmap, private val username: String, private val listener: OnImageUploadListener) {

    interface OnImageUploadListener {
        fun onImageUploadSuccess(response: String)
        fun onImageUploadFailure(errorMessage: String)
    }

    fun execute() {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val url = "https://api.imgbb.com/1/upload?key=801132d5b786bcf0c937aeba2cf75d6f"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "${username}_profile.jpg", byteArray.toRequestBody("image/*".toMediaTypeOrNull()))
                .build())
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                listener.onImageUploadFailure(e.message!!)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                listener.onImageUploadSuccess(response.body!!.string())
            }
        })
    }

}