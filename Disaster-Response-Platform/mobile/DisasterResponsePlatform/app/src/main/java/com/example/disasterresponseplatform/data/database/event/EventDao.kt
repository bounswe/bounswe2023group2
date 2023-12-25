package com.example.disasterresponseplatform.data.database.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.Need

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM ${DatabaseInfo.EVENT}")
    fun getAllEvents(): List<Event>?

    @Query("DELETE FROM ${DatabaseInfo.EVENT} WHERE ${EventCols.id} = :id")
    fun deleteEvent(id: Int)


}