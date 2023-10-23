package com.example.disasterresponseplatform.data.database.need

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Entity(tableName = DatabaseInfo.NEED)
data class Need(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = NeedCols.id)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = NeedCols.creatorID)
    val creatorID: String,
    @ColumnInfo(name = NeedCols.type)
    val type: String,
    @ColumnInfo(name = NeedCols.subTypeList)
    val subType: String,
    @ColumnInfo(name = NeedCols.creationTime)
    val creationTime: String?,
    @ColumnInfo(name = NeedCols.quantity)
    val quantity: Int?,
    @ColumnInfo(name = NeedCols.location)
    val location: String?,
    @ColumnInfo(name = NeedCols.urgency)
    val urgency: Int?
)
