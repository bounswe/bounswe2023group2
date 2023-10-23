package com.example.disasterresponseplatform.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtil {
    companion object{
        fun getDate(format: String?): String? {
            val calDate: Date = Calendar.getInstance().time
            return SimpleDateFormat(format, Locale.getDefault()).format(calDate)
        }

        fun getTime(format: String?): String? {
            val calDate: Date = Calendar.getInstance().time
            return SimpleDateFormat(format, Locale.getDefault()).format(calDate)
        }
    }
}