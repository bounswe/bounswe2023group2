package com.example.disasterresponseplatform.data.database.emergency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo
import com.example.disasterresponseplatform.data.database.action.ActionCols

@Entity(tableName = DatabaseInfo.ACTION)
data class Emergency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ActionCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = ActionCols.creatorID)
    val creatorID: String,
    @ColumnInfo(name = ActionCols.type)
    val type: String,
    @ColumnInfo(name = ActionCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = ActionCols.quantity)
    val quantity: Int?,
    @ColumnInfo(name = ActionCols.startLocation)
    val startLocation: String?,
    @ColumnInfo(name = ActionCols.endLocation)
    val endLocation: String?,
    @ColumnInfo(name = ActionCols.urgency)
    val urgency: Int?
)