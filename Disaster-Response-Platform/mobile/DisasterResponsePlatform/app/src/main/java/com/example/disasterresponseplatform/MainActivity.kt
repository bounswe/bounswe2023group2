package com.example.disasterresponseplatform

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.disasterresponseplatform.managers.DataResponse
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.models.enums.Endpoint
import com.example.disasterresponseplatform.models.enums.RequestType
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
                requestType = RequestType.GET,  // Specify the type of request here
                headers = headers,
                callback = object : retrofit2.Callback<DataResponse> {
                    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Status Code: ${response.code()}, Successful Response!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Status Code: ${response.code()}, Error Message: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }
}