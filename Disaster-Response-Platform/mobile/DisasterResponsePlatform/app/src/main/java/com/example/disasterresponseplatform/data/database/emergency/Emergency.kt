package com.example.disasterresponseplatform.data.database.emergency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.action.ActionCols

@Entity(tableName = DatabaseInfo.EMERGENCY)
data class Emergency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EmergencyCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = EmergencyCols.creatorName)
    val creatorName: String,
    @ColumnInfo(name = EmergencyCols.type)
    val type: String,
    @ColumnInfo(name = EmergencyCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = EmergencyCols.lastUpdateTime)
    val lastUpdateTime: String?,
    @ColumnInfo(name = EmergencyCols.location)
    val location: String?,
    @ColumnInfo(name = EmergencyCols.contactNumber)
    val contactNumber: String?,
    @ColumnInfo(name = EmergencyCols.notes)
    val notes: String?
)
