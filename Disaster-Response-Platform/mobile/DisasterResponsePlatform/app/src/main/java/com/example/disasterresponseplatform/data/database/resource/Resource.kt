package com.example.disasterresponseplatform.data.database.resource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.disasterresponseplatform.data.database.DatabaseInfo

@Entity(tableName = DatabaseInfo.RESOURCE)
data class Resource(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMNS.ID)
    val ID: Int?, // if user does not enter ID, it generates it automatically
    @ColumnInfo(name = COLUMNS.CONDITION)
    val condition: String,
    @ColumnInfo(name = COLUMNS.INITIAL_QUANTITY)
    val initialQuantity: Int,
    @ColumnInfo(name = COLUMNS.CURRENT_QUANTITY)
    val currentQuantity: Int
) {
    companion object COLUMNS {
        const val ID = "id"
        const val CONDITION = "condition"
        const val INITIAL_QUANTITY = "initial_quantity"
        const val CURRENT_QUANTITY = "current_quantity"
    }
}