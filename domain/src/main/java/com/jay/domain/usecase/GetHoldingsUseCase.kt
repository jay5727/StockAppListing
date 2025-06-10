package com.jay.domain.usecase

import com.jay.domain.mapper.HoldingSummaryMapper
import com.jay.domain.model.HoldingSummary
import com.jay.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHoldingsUseCase(
    private val repository: HoldingsRepository,
    private val mapper: HoldingSummaryMapper
) {
    operator fun invoke(): Flow<Result<HoldingSummary>> {
        return repository.getHoldings().map { result ->
            result.fold(
                onSuccess = { list ->
                    runCatching { mapper.map(list) }
                },
                onFailure = { throwable ->
                    Result.failure(throwable)
                }
            )
        }
    }
}