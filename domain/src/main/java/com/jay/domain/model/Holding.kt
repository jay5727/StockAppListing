package com.jay.domain.model

data class Holding(
    val symbol: String,
    val quantity: Int,
    val ltp: Double?,
    val avgPrice: Double,
    val close: Double
)

data class HoldingSummary(
    val holdingsList: List<Holding>,
    val investmentInfo: InvestmentInfo?=null
)