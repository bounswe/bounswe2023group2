package com.example.disasterresponseplatform.data.database.resource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.need.NeedResourceCols
import com.example.disasterresponseplatform.data.enums.NeedTypes
import com.example.disasterresponseplatform.data.enums.ResourceCondition

@Entity(tableName = DatabaseInfo.RESOURCE)
data class Resource(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NeedResourceCols.id)
    val ID: Long?, // if user does not enter ID, it generates it automatically
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
    @ColumnInfo(name = NeedResourceCols.location)
    val location: String?,
)
