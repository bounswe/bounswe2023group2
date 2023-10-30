package com.example.disasterresponseplatform.data.database.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface EventDao {
    @Insert
    suspend fun insertActivation(event: Event)

    @Query("SELECT ${EventCols.location} FROM ${DatabaseInfo.EVENT} WHERE ${EventCols.creatorID} = :creatorID")
    fun getLocation(creatorID: String): String
}