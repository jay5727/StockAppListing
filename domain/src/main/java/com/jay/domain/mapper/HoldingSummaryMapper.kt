package com.jay.domain.mapper

import com.jay.domain.model.Holding
import com.jay.domain.model.HoldingSummary
import com.jay.domain.model.InvestmentInfo
import com.jay.domain.util.getRoundUpto2Decimals
import com.jay.domain.util.orZero

class HoldingSummaryMapper {

    fun map(holdingList: List<Holding>): HoldingSummary {
        val currentValue = getCurrentValue(holdingList)
        val totalInvestment = getTotalInvestment(holdingList)

        val investmentInfo = InvestmentInfo(
            currentValue = currentValue,
            totalInvestment = totalInvestment,
            totalPNL = currentValue - totalInvestment,
            todaysPNL = getDayPnL(holdingList),
            percentageChange = getPercentageChange(holdingList)
        )

        return HoldingSummary(holdingsList = holdingList, investmentInfo = investmentInfo)
    }

    /**
     * Returns the total current value of the holdings
     */
    private fun getCurrentValue(holdingList: List<Holding>) =
        holdingList.sumOf { getCurrentValue(it) }

    /**
     * Returns the total investment value of the holdings
     */
    private fun getTotalInvestment(holdingList: List<Holding>) =
        holdingList.sumOf { getInvestmentValue(it) }

    /**
     * Returns the individual stock current value of the holdings
     */
    private fun getCurrentValue(holding: Holding) =
        holding.ltp.orZero() * holding.quantity.orZero()

    /**
     * Returns the individual stock invested value of the holdings
     */
    private fun getInvestmentValue(holding: Holding?) =
        holding?.avgPrice?.orZero()?.times(holding.quantity.orZero()).orZero()

    /**
     * Returns the day pnl of all the holdings
     */
    private fun getDayPnL(list: List<Holding>) = list.sumOf {
        (it.close.orZero() - it.ltp.orZero()) * it.quantity.orZero()
    }

    /**
     * Returns the todays total pnl of all the holdings based on LTP-AvgPrice
     */
    private fun getTodaysPnl(list: List<Holding>): Double {
        return list.sumOf { (it.ltp.orZero() - it.avgPrice.orZero()) * it.quantity.orZero() }
    }

    /**
     * Returns the closing pnl of all the holdings based on Close-AvgPrice
     */
    private fun getClosingPnl(list: List<Holding>): Double {
        return list.sumOf { (it.close.orZero() - it.avgPrice.orZero()) * it.quantity.orZero() }
    }

    /**
     * Returns the percentage change in pnl of all the holdings
     */
    private fun getPercentageChange(list: List<Holding>): Double {
        val totalPnlClose = getClosingPnl(list)
        val totalPnlToday = getTodaysPnl(list)
        val percentageChange = if (totalPnlClose != 0.0) {
            ((totalPnlToday - totalPnlClose) / totalPnlClose) * 100
        } else {
            0.0
        }
        return percentageChange.getRoundUpto2Decimals()
    }
}
