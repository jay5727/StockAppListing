package com.jay.domain.model

data class InvestmentInfo(
    val currentValue: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val todaysPNL: Double = 0.0,
    val totalPNL: Double = 0.0,
    val percentageChange: Double = 0.0,
)
