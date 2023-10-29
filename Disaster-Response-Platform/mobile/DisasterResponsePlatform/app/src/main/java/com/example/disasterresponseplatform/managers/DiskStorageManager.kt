package com.example.disasterresponseplatform.managers

import android.content.Context
import android.content.SharedPreferences

object DiskStorageManager {
    private lateinit var sharedPreferences: SharedPreferences

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }

    fun setKeyValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getKeyValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
