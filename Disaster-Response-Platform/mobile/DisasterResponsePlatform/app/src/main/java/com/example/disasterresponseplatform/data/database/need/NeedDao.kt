package com.example.disasterresponseplatform.data.database.need

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.event.EventCols

@Dao
interface NeedDao {
    @Insert
    suspend fun insertNeed(need: Need)

    @Query("SELECT * FROM ${DatabaseInfo.NEED}")
    fun getAllNeeds(): List<Need>?

    @Query("DELETE FROM ${DatabaseInfo.NEED} WHERE ${NeedResourceCols.id} = :id")
    fun deleteNeed(id: Int)
}