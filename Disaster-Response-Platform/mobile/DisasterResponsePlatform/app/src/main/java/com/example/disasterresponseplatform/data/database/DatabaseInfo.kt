package com.example.disasterresponseplatform.data.database

/**
 * This is for holding data in regular way.
 */
class DatabaseInfo {
    companion object {
        const val DATABASE: String = "DARP_DB"
        const val ACTION: String = "ACTION"
        const val EMERGENCY: String = "EMERGENCY"
        const val EVENT: String = "EVENT"
        const val NEED: String = "NEED"
        const val RESOURCE: String = "RESOURCE"
        const val DATABASE_VERSION: Int = 2 // you need to change that whenever you change any table on DB
    }
}
