package com.jay.presentation.model

import com.jay.domain.model.InvestmentInfo

data class HoldingSummaryUI(
    val holdingList: List<HoldingUIModel>,
    val investmentInfo: InvestmentInfo?
)