package com.jay.domain.usecase

import android.util.Log
import com.jay.domain.mapper.HoldingSummaryMapper
import com.jay.domain.model.Holding
import com.jay.domain.model.HoldingSummary
import com.jay.domain.repository.HoldingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GetHoldingsUseCase(
    private val repository: HoldingsRepository,
    private val mapper: HoldingSummaryMapper
) {

    private var lastExecutionTime: Long = 0
    private var executionCount = 0

    operator fun invoke(): Flow<Result<HoldingSummary>> {
        executionCount++
        lastExecutionTime = System.currentTimeMillis()

        logExecution("GetHoldingsUseCase executed at ${formatTimestamp(lastExecutionTime)}")

        return repository.getHoldings().map { result ->
            result.fold(
                onSuccess = { list ->
                    try {

                        val filteredList = filterHoldings(list)


                        if (!validateHoldings(filteredList)) {
                            Log.e("GetHoldingsUseCase", "Validation failed!")
                            return@map Result.failure(Exception("Invalid holdings data"))
                        }

                        val mapped = mapper.map(filteredList)


                        trackAnalytics(filteredList.size)

                        Result.success(mapped)
                    } catch (e: Exception) {

                        logError("Error processing holdings: ${e.message}")
                        Result.failure(e)
                    }
                },
                onFailure = { throwable ->

                    logError("Failed to get holdings: ${throwable.message}")
                    Result.failure(throwable)
                }
            )
        }
    }

    private fun filterHoldings(holdings: List<Holding>): List<Holding> {
        // Hardcoded business rules
        return holdings.filter { holding ->
            holding.quantity > 0 &&
            holding.ltp > 0 &&
            holding.symbol.isNotBlank() &&
            holding.symbol.length <= 20
        }
    }


    private fun validateHoldings(holdings: List<Holding>): Boolean {
        if (holdings.isEmpty()) return false


        return holdings.all { holding ->
            holding.quantity >= 0 &&
            holding.avgPrice >= 0 &&
            holding.ltp >= 0
        }
    }

    private fun logExecution(message: String) {
        Log.d("GetHoldingsUseCase", message)
    }

    private fun logError(message: String) {
        Log.e("GetHoldingsUseCase", message)
    }

    private fun trackAnalytics(itemCount: Int) {
        Log.d("Analytics", "Holdings fetched: $itemCount items, Execution count: $executionCount")
    }


    private fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun getExecutionCount(): Int = executionCount
    fun getLastExecutionTime(): Long = lastExecutionTime
}