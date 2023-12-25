package com.example.disasterresponseplatform.managers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.io.Serializable

object DiskStorageManager {
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    fun setKeyValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun hasKey(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun getKeyValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     * Checks whether it has a not null token (user logs in)
     */
    fun checkToken(): Boolean{
        val token = getKeyValue("token")
        Log.i("token", "Token $token")
        return (hasKey("token") and !token.isNullOrEmpty())
    }

    /**
     * Checks whether current user's username is the same as requested
     */
    fun checkUsername(username: String): Boolean{
        val currentUsername = getKeyValue("username").toString()
        return (currentUsername == username)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
