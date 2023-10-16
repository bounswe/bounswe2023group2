package com.example.disasterresponseplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.disasterresponseplatform.managers.DataResponse
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.models.enums.Endpoint
import retrofit2.Call
import retrofit2.Response



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.myButton)

        button.setOnClickListener {
            // Handle the button click event

            val networkManager = NetworkManager()

            val headers = mapOf("Authorization" to "Bearer YOUR_TOKEN")

            networkManager.makeRequest(
                endpoint = Endpoint.DATA,
                headers = headers,
                callback = object : retrofit2.Callback<DataResponse> {
                    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Failed response!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                        Toast.makeText(this@MainActivity, "Successful Response!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}