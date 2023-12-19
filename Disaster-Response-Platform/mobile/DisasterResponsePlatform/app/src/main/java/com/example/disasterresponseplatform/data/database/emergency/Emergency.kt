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
    @ColumnInfo(name = EmergencyCols.creatorName)
    val creatorName: String, // this is for controlling who creates this. If 2 different user create when no internet connection
    @ColumnInfo(name = EmergencyCols.type)
    val type: String,
    @ColumnInfo(name = EmergencyCols.shortDescription)
    val shortDescription: String,
    @ColumnInfo(name = EmergencyCols.additionalNotes)
    val additionalNotes: String
)
