package com.jay.domain.usecase

import com.jay.domain.mapper.HoldingSummaryMapper
import com.jay.domain.model.HoldingSummary
import com.jay.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class BrokenHoldingsUseCase(
    private val repository: HoldingsRepository,
    private val mapper: HoldingSummaryMapper
) : BaseUseCase<HoldingSummary>() {

    private var executionCount = 0
    private var isCleanedUp = false

    override fun canExecute(): Boolean {
        executionCount++
        if (executionCount > 5) {
            throw IllegalStateException("Too many executions! This shouldn't happen in base class")
        }
        return executionCount % 2 == 0 // Only execute on even counts
    }


    // This implementation returns empty flow if cleaned up, breaking the contract
    override operator fun invoke(): Flow<Result<HoldingSummary>> {
        if (isCleanedUp) {
            return flow { }
        }

        if (!canExecute()) {
            return flow {
                emit(Result.failure(Exception("Cannot execute now")))
            }
        }

        return if (executionCount % 3 == 0) {
            flow {
                emit(Result.success(HoldingSummary(emptyList(), null)))
            }
        } else {
            repository.getHoldings().map { result ->
                result.fold(
                    onSuccess = { list ->
                        try {
                            val mapped = mapper.map(list)
                            Result.success(mapped)
                        } catch (e: Exception) {
                            Result.failure(e)
                        }
                    },
                    onFailure = { throwable ->
                        Result.failure(throwable)
                    }
                )
            }
        }
    }


    override fun cleanup() {
        if (isCleanedUp) {
            throw IllegalStateException("Already cleaned up! Double cleanup not allowed!")
        }
        isCleanedUp = true
        executionCount = 0
    }
}
