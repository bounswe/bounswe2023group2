package com.example.disasterresponseplatform.data.database.emergency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Entity(tableName = DatabaseInfo.EMERGENCY)
data class Emergency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EmergencyCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = EmergencyCols.type)
    val type: String,
    @ColumnInfo(name = EmergencyCols.description)
    val description: String?,
    @ColumnInfo(name = EmergencyCols.contactName)
    val contactName: String?,
    @ColumnInfo(name = EmergencyCols.contactNumber)
    val contactNumber: String?,
    @ColumnInfo(name = EmergencyCols.location)
    val location: String?,
    @ColumnInfo(name = EmergencyCols.x)
    val x: Double,
    @ColumnInfo(name = EmergencyCols.y)
    val y: Double,
)