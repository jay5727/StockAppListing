package com.jay.data.repository

import com.jay.data.di.IoDispatcher
import com.jay.data.mapper.toDomain
import com.jay.data.network.HoldingApiService
import com.jay.domain.model.Holding
import com.jay.domain.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: HoldingApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HoldingsRepository {

    override fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        val result: Result<List<Holding>> = runCatching {
            val response = apiService.getHoldingResponse()
            val dtoList = response.data.userHolding
            dtoList.map { it.toDomain() }
        }
        emit(result)
    }.flowOn(ioDispatcher)
}
