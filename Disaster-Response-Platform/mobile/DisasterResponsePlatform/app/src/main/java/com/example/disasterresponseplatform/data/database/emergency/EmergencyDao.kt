package com.example.disasterresponseplatform.data.database.emergency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo


@Dao
interface EmergencyDao {
    @Insert
    suspend fun insertEmergency(emergency: Emergency)

    @Query("SELECT ${EmergencyCols.notes} FROM ${DatabaseInfo.EMERGENCY} WHERE ${EmergencyCols.creatorName} = :creatorName")
    fun getNotes(creatorName: String): String
}