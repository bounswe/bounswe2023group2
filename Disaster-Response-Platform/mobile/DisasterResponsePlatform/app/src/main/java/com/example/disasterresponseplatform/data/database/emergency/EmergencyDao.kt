package com.example.disasterresponseplatform.data.database.emergency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.event.EventCols
import com.example.disasterresponseplatform.data.database.need.Need


@Dao
interface EmergencyDao {
    @Insert
    suspend fun insertEmergency(emergency: Emergency)

    @Query("SELECT * FROM ${DatabaseInfo.EMERGENCY}")
    fun getAllEmergencies(): List<Emergency>?

    @Query("DELETE FROM ${DatabaseInfo.EMERGENCY} WHERE ${EmergencyCols.id} = :id")
    fun deleteEmergency(id: Int)
}