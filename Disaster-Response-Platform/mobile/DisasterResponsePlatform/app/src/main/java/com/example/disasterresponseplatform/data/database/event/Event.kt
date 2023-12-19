package com.example.disasterresponseplatform.data.database.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.emergency.EmergencyCols

@Entity(tableName = DatabaseInfo.EVENT)
data class Event(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = EventCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = EventCols.creatorName)
    val creatorName: String, // this is for controlling who creates this. If 2 different user create when no internet connection
    @ColumnInfo(name = EventCols.type)
    val type: String,
    @ColumnInfo(name = EventCols.shortDescription)
    val shortDescription: String,
    @ColumnInfo(name = EventCols.additionalNotes)
    val additionalNotes: String
)
