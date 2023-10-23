package com.example.disasterresponseplatform.data.database.need

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface NeedDao {
    @Insert
    suspend fun insertActivation(need: Need)

    @Query("SELECT ${NeedCols.location} FROM ${DatabaseInfo.NEED}  WHERE ${NeedCols.creatorID} = :creatorID")
    fun getLocation(creatorID: String): String
}