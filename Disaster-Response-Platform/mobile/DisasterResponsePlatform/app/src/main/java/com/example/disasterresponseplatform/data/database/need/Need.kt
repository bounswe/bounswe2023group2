package com.example.disasterresponseplatform.data.database.need

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.enums.NeedTypes

@Entity(tableName = DatabaseInfo.NEED)
data class Need(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = NeedResourceCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = NeedResourceCols.creatorName)
    val creatorName: String,
    @ColumnInfo(name = NeedResourceCols.type)
    val type: NeedTypes,
    @ColumnInfo(name = NeedResourceCols.details)
    val details: String,
    @ColumnInfo(name = NeedResourceCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = NeedResourceCols.quantity)
    val quantity: Int?,
    @ColumnInfo(name = NeedResourceCols.location)
    val location: String?,
    @ColumnInfo(name = NeedResourceCols.urgency)
    val urgency: Int?
)
