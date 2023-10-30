package com.example.disasterresponseplatform.data.database.action

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface ActionDao {
    @Insert
    suspend fun insertActivation(action: Action)

    @Query("SELECT ${ActionCols.startLocation} FROM ${DatabaseInfo.ACTION} WHERE ${ActionCols.creatorID} = :creatorID")
    fun getStartLocation(creatorID: String): String
}