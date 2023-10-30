package com.example.disasterresponseplatform.data.database.event

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Entity(tableName = DatabaseInfo.EVENT)
data class Event(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = EventCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = EventCols.creatorID)
    val creatorID: String,
    @ColumnInfo(name = EventCols.type)
    val type: String,
    @ColumnInfo(name = EventCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = EventCols.location)
    val location: String?
)
