package com.jay.domain.model

data class InvestmentInfo(
    var currentValue: Double = 0.0,
    var totalInvestment: Double = 0.0,
    var todaysPNL: Double = 0.0,
    var totalPNL: Double = 0.0,
    var percentageChange: Double = 0.0,
)
