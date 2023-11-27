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

        /**
         * It turns date from DD/MM/YYYY to YYYY-MM-DD
         */
        fun dateForBackend(originalDateStr: String): String {
            // Define the regex pattern for "d/M/yyyy" or "dd/MM/yyyy"
            val regexPattern = """(\d{1,2})/(\d{1,2})/(\d{4})""".toRegex()
            // Use regex to match and rearrange the groups
            val formattedDateStr = regexPattern.replace(originalDateStr) {
                // Rearrange the groups to form "yyyy-MM-dd"
                "${it.groupValues[3]}-${it.groupValues[2].padStart(2, '0')}-${it.groupValues[1].padStart(2, '0')}"
            }
            return formattedDateStr
        }
    }
}