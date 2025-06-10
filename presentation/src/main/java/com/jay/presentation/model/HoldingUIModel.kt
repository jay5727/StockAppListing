package com.jay.presentation.model

import com.jay.domain.model.Holding

data class HoldingUIModel(
    val holding: Holding,
    val individualStockPNL: Double,
    val formattedLTP: String,
    val formattedPNL: String
)