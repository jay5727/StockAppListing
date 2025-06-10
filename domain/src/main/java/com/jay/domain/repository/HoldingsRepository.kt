package com.jay.domain.repository

import com.jay.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    fun getHoldings(): Flow<Result<List<Holding>>>
}