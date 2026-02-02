package com.jay.presentation.mapper

import com.jay.domain.model.Holding
import com.jay.domain.util.formatAmount
import com.jay.domain.util.getFormattedString
import com.jay.domain.util.getRoundUpto2Decimals
import com.jay.domain.util.orZero
import com.jay.presentation.model.HoldingUIModel
import javax.inject.Inject

class HoldingUIMapper @Inject constructor() {

    private fun toUIModel(holding: Holding): HoldingUIModel {
        val pnl = (holding.ltp.orZero() - holding.avgPrice.orZero()) * holding.quantity
        return HoldingUIModel(
            holding = holding,
            individualStockPNL = pnl,
            formattedLTP = holding.ltp.toString().formatAmount(),
            formattedPNL = pnl.getRoundUpto2Decimals().getFormattedString()
        )
    }

    fun toUIModelList(holdings: List<Holding>) = holdings.map { toUIModel(it) }

}
