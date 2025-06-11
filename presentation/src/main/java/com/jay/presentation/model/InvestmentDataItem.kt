package com.jay.presentation.model

data class InvestmentDataItem(
    val label: String,
    val value: String,
    val amount: Double = 0.0,
    val showPnlColor: Boolean = false,
    val percentageChange: Double = 0.0
)