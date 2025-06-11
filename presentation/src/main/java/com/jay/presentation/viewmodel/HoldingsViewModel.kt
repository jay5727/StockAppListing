package com.jay.presentation.viewmodel

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
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(
    private val getHoldingsUseCase: GetHoldingsUseCase,
    private val mapper: HoldingUIMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<HoldingScreenUiState>(HoldingScreenUiState.Loading)
    val uiState: StateFlow<HoldingScreenUiState> = _uiState

    init {
        fetchHoldings()
    }
    
    fun fetchHoldings() {
        _uiState.value = HoldingScreenUiState.Loading
        viewModelScope.launch {
            getHoldingsUseCase().collect { result ->
                result.onSuccess { holdingSummary ->

                    val uiSummary = HoldingSummaryUI(
                        holdingList = mapper.toUIModelList(holdingSummary.holdingsList),
                        investmentInfo = holdingSummary.investmentInfo
                    )
                    _uiState.value =
                        HoldingScreenUiState.Success(summary = uiSummary)
                }.onFailure { error ->
                    _uiState.value =
                        HoldingScreenUiState.Error(error.message ?: "Something went wrong")
                }
            }
        }

    }
}
