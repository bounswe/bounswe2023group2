package com.example.disasterresponseplatform.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import androidx.core.net.toFile
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import okhttp3.Headers
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
import kotlin.random.Random

class FileUploadTask(private val uri: Uri, private val username: String, private val contentResolver: ContentResolver, private val listener: OnFileUploadListener) {

    interface OnFileUploadListener {
        fun onFileUploadSuccess(response: String)
        fun onFileUploadFailure(errorMessage: String)
    }

    fun execute() {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val inputStream = contentResolver.openInputStream(uri)
        println("Gonderiyorum: $uri")
        val filename = "${username}_${Random.nextInt(0, 1000000)}"
        inputStream?.copyTo(byteArrayOutputStream)
        inputStream?.close()
        val byteArray = byteArrayOutputStream.toByteArray()
        val url = "http://3.218.226.215:8000/api/uploadfile"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .headers(Headers.headersOf("Authorization", "Bearer ${DiskStorageManager.getKeyValue("token")}"))
            .post(MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, byteArray.toRequestBody("*/*".toMediaTypeOrNull()))
                .build())
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                listener.onFileUploadFailure(e.message!!)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                listener.onFileUploadSuccess(response.body!!.string())
            }
        })
    }

}