package com.jay.presentation.state
import com.jay.presentation.model.HoldingSummaryUI

sealed class HoldingScreenUiState {
    data object Loading : HoldingScreenUiState()
    data class Success(val summary: HoldingSummaryUI) : HoldingScreenUiState()
    data class Error(val message: String ?= null) : HoldingScreenUiState()
}