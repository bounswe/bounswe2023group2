package com.example.disasterresponseplatform.data.database.need

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Dao
interface NeedDao {
    @Insert
    suspend fun insertNeed(need: Need)

    @Query("SELECT ${NeedResourceCols.coordinateX} FROM ${DatabaseInfo.NEED}  WHERE ${NeedResourceCols.creatorName} = :creatorID")
    fun getX(creatorID: String): Double?

    @Query("SELECT ${NeedResourceCols.coordinateY} FROM ${DatabaseInfo.NEED}  WHERE ${NeedResourceCols.creatorName} = :creatorID")
    fun getY(creatorID: String): Double?

    @Query("SELECT * FROM ${DatabaseInfo.NEED} ORDER BY ${NeedResourceCols.id}")
    fun getAllNeeds(): List<Need>?

    @Update
    suspend fun updateNeed(need: Need)
}