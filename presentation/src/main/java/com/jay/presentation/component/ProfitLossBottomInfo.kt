package com.jay.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jay.domain.model.InvestmentInfo
import com.jay.domain.util.StringConstants.CurrentValue
import com.jay.domain.util.StringConstants.PNL
import com.jay.domain.util.StringConstants.TodaysPL
import com.jay.domain.util.StringConstants.TotalInvestment
import com.jay.domain.util.getFormattedString
import com.jay.presentation.model.InvestmentDataItem

@Composable
fun ProfitLossBottomSheetInfo(
    modifier: Modifier = Modifier,
    detailInfo: InvestmentInfo
) {
    var showInfoSheet by remember {
        mutableStateOf(false)
    }
    val list = listOf(
        InvestmentDataItem(
            label = CurrentValue,
            value = detailInfo.totalInvestment.getFormattedString()
        ),
        InvestmentDataItem(
            label = TotalInvestment,
            value = detailInfo.currentValue.getFormattedString()
        ),
        InvestmentDataItem(
            label = TodaysPL,
            value = detailInfo.todaysPNL.getFormattedString(),
            amount = detailInfo.todaysPNL, showPnlColor = true
        ),
        InvestmentDataItem(
            label = PNL,
            value = detailInfo.totalPNL.getFormattedString(),
            amount = detailInfo.totalPNL,
            percentageChange = detailInfo.percentageChange,
            showPnlColor = true
        )
    )
    Column(modifier = modifier.padding(8.dp)) {
        AnimatedVisibility(
            modifier = Modifier.testTag("expanded_sheet"),
            visible = showInfoSheet,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + expandVertically(),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkVertically()
        ) {
            Column {
                list.dropLast(1).forEachIndexed { index, it ->
                    InvestmentDetails(
                        modifier = Modifier.padding(8.dp),
                        item = it
                    )
                    if (index == list.dropLast(1).lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }

            }
        }

        //Profit & Loss
        InvestmentDetails(
            modifier = Modifier.padding(8.dp),
            item = list.last(),
            shouldShowIcon = true,
            onPnLClicked = { showInfoSheet = !showInfoSheet }
        )
    }
}

@Preview
@Composable
private fun ProfitLossBottomSheetInfoScreenPreview() {
    ProfitLossBottomSheetInfo(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
        detailInfo = InvestmentInfo(
            currentValue = 2979507.0,
            totalInvestment = 2906545.95,
            todaysPNL = -31841.15,
            totalPNL = 72961.05,
            percentageChange = 77.44
        )
    )
}