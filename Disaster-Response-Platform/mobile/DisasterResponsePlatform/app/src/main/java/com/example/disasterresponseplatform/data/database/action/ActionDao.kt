package com.example.disasterresponseplatform.data.database.action

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.Need

@Dao
interface ActionDao {
    @Insert
    suspend fun insertAction(action: Action)

    @Query("SELECT * FROM ${DatabaseInfo.ACTION}")
    fun getAllActions(): List<Action>?

    @Query("SELECT ${ActionCols.startLocation} FROM ${DatabaseInfo.ACTION} WHERE ${ActionCols.creatorID} = :creatorID")
    fun getStartLocation(creatorID: String): String

    @Update
    suspend fun updateAction(action: Action)
}