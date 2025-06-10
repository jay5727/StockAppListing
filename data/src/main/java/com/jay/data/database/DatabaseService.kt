package com.jay.data.database

interface DatabaseService {

    suspend fun insertHoldingList(countries: List<HoldingEntity>)

    suspend fun getHoldingList(): List<HoldingEntity>
}