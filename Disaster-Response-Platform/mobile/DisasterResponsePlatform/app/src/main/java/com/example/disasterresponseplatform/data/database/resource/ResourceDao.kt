package com.example.disasterresponseplatform.data.database.resource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.Need
import com.example.disasterresponseplatform.data.database.need.NeedResourceCols

@Dao
interface ResourceDao {

    @Insert
    suspend fun insertResource(resource: Resource)

    @Query("SELECT ${NeedResourceCols.location} FROM ${DatabaseInfo.RESOURCE}  WHERE ${NeedResourceCols.creatorName} = :creatorName")
    fun getLocation(creatorName: String): String?

    @Query("SELECT * FROM ${DatabaseInfo.RESOURCE} ORDER BY ${NeedResourceCols.id}")
    fun getAllResources(): List<Resource>?
}
