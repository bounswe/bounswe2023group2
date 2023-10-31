package com.example.disasterresponseplatform.data.database.need

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface NeedDao {
    @Insert
    suspend fun insertNeed(need: Need)

    @Query("SELECT ${NeedResourceCols.location} FROM ${DatabaseInfo.NEED}  WHERE ${NeedResourceCols.creatorName} = :creatorID")
    fun getLocation(creatorID: String): String?

    @Query("SELECT * FROM ${DatabaseInfo.NEED} ORDER BY ${NeedResourceCols.id}")
    fun getAllNeeds(): List<Need>?
}