package com.example.disasterresponseplatform.utils

import java.util.Random

class StringUtil {
    companion object{
        fun generateRandomStringID(): String {
            val chars = "abcdef0123456789"
            val stringBuilder = StringBuilder(24)

            repeat(24) {
                val randomIndex = Random().nextInt(chars.length)
                stringBuilder.append(chars[randomIndex])
            }

            return stringBuilder.toString()
        }
        }
}