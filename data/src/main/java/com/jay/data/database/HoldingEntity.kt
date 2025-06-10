package com.jay.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "holding_table"

@Entity(tableName = TABLE_NAME)
data class HoldingEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double
)
