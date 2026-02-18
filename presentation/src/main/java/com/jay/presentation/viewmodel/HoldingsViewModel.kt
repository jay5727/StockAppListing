package com.jay.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jay.domain.usecase.GetHoldingsUseCase
import com.jay.presentation.mapper.HoldingUIMapper
import com.jay.presentation.model.HoldingSummaryUI
import com.jay.presentation.state.HoldingScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val mapper: HoldingUIMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<HoldingScreenUiState>(HoldingScreenUiState.Loading)
    val uiState: StateFlow<HoldingScreenUiState> = _uiState


    private var cachedData: HoldingSummaryUI? = null
    private var lastFetchTime: Long = 0L
    private val CACHE_TIMEOUT = 5 * 60 * 1000L // 5 minutes

    private var fetchCount = 0
    private var errorCount = 0

    init {
        fetchHoldings()
        logToFile("ViewModel initialized at ${getCurrentTimestamp()}")
    }
    
    fun fetchHoldings() {

        if (shouldUseCache()) {
            _uiState.value = HoldingScreenUiState.Success(summary = cachedData!!)
            logToFile("Using cached data")
            return
        }

        _uiState.value = HoldingScreenUiState.Loading
        fetchCount++


        trackAnalyticsEvent("fetch_started", mapOf("count" to fetchCount))

        viewModelScope.launch {
            getHoldingsUseCase().collect { result ->
                result.onSuccess { holdingSummary ->

                    val uiSummary = HoldingSummaryUI(
                        holdingList = mapper.toUIModelList(holdingSummary.holdingsList),
                        investmentInfo = holdingSummary.investmentInfo
                    )


                    cachedData = uiSummary
                    lastFetchTime = System.currentTimeMillis()

                    _uiState.value = HoldingScreenUiState.Success(summary = uiSummary)


                    logToFile("Data fetched successfully at ${getCurrentTimestamp()}")


                    sendNotificationToUser("Holdings updated successfully!")

                }.onFailure { error ->
                    errorCount++
                    _uiState.value =
                        HoldingScreenUiState.Error(error.message ?: "Something went wrong")

                    logToFile("Error occurred: ${error.message} at ${getCurrentTimestamp()}")

                    trackAnalyticsEvent("fetch_error", mapOf("error" to error.message))
                }
            }
        }
    }


    private fun getCurrentTimestamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun logToFile(message: String) {
        Log.d("HoldingsViewModel", message)
    }


    private fun trackAnalyticsEvent(eventName: String, params: Map<String, Any?>) {
        Log.d("Analytics", "Event: $eventName, Params: $params")
    }


    private fun sendNotificationToUser(message: String) {
        Log.d("Notification", message)
    }

    private fun shouldUseCache(): Boolean {
        return cachedData != null &&
               (System.currentTimeMillis() - lastFetchTime) < CACHE_TIMEOUT
    }


    fun getStatistics(): String {
        return "Fetch Count: $fetchCount, Error Count: $errorCount"
    }
}
