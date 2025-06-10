package com.jay.data.database

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseServiceImpl @Inject constructor(
    private val dao: HoldingDao
) : DatabaseService {
    override suspend fun insertHoldingList(countries: List<HoldingEntity>) {
        dao.insertHoldingList(countries)
    }

    override suspend fun getHoldingList(): List<HoldingEntity> {
       return dao.getHoldingList()
    }
}