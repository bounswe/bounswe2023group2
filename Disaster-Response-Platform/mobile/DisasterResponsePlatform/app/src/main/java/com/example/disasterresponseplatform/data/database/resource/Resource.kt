package com.example.disasterresponseplatform.data.database.resource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.NeedResourceCols
import com.example.disasterresponseplatform.data.enums.NeedTypes

@Entity(tableName = DatabaseInfo.RESOURCE)
data class Resource(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = NeedResourceCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = NeedResourceCols.creatorName)
    val creatorName: String, // this is for controlling who creates this. If 2 different user create when no internet connection
    @ColumnInfo(name = NeedResourceCols.type)
    val type: String,
    @ColumnInfo(name = NeedResourceCols.shortDescription)
    val shortDescription: String,
    @ColumnInfo(name = NeedResourceCols.additionalNotes)
    val additionalNotes: String,
    @ColumnInfo(name = NeedResourceCols.quantity)
    val quantity: Int = 1,
)

