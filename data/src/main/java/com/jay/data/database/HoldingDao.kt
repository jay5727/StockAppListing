package com.jay.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HoldingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldingList(holdings: List<HoldingEntity>)

    @Query("SELECT * FROM holding_table")
    suspend fun getHoldingList(): List<HoldingEntity>
}
