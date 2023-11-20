package com.example.disasterresponseplatform.data.database.resource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.NeedResourceCols
import com.example.disasterresponseplatform.data.enums.NeedTypes

@Entity(tableName = DatabaseInfo.RESOURCE)
data class Resource(
    @PrimaryKey()
    @ColumnInfo(name = NeedResourceCols.id)
    val ID: String,
    @ColumnInfo(name = NeedResourceCols.creatorName)
    val creatorName: String,
    @ColumnInfo(name = NeedResourceCols.condition)
    val condition: String,
    @ColumnInfo(name = NeedResourceCols.quantity)
    val quantity: Int,
    @ColumnInfo(name = NeedResourceCols.type)
    val type: NeedTypes,
    @ColumnInfo(name = NeedResourceCols.details)
    val details: String,
    @ColumnInfo(name = NeedResourceCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = NeedResourceCols.coordinateX)
    val coordinateX: Double,
    @ColumnInfo(name = NeedResourceCols.coordinateY)
    val coordinateY: Double,
)

