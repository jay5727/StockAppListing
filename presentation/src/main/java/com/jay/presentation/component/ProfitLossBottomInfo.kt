package com.jay.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jay.domain.model.InvestmentInfo
import com.jay.domain.util.StringConstants.CurrentValue
import com.jay.domain.util.StringConstants.PNL
import com.jay.domain.util.StringConstants.TodaysPL
import com.jay.domain.util.StringConstants.TotalInvestment
import com.jay.domain.util.getFormattedString
import com.jay.presentation.theme.LightGrey

@Composable
fun ProfitLossBottomSheetInfo(
    modifier: Modifier = Modifier,
    detailInfo: InvestmentInfo
) {
    var showInfoSheet by remember {
        mutableStateOf(false)
    }
    val totalPNL = detailInfo.totalPNL
    val todaysPNL = detailInfo.todaysPNL
    val percentageChange = detailInfo.percentageChange

    val list = listOf(
        Pair(CurrentValue, detailInfo.currentValue.getFormattedString()),
        Pair(TotalInvestment, detailInfo.totalInvestment.getFormattedString()),
        Pair(TodaysPL, detailInfo.todaysPNL.getFormattedString()),
        Pair(PNL, detailInfo.totalPNL.getFormattedString())
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
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
                            pair = it,
                            shouldSetPnlColor = index == list.lastIndex - 1,
                            value = todaysPNL
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                }
            }

            //Profit & Loss
            InvestmentDetails(
                modifier = Modifier.padding(8.dp),
                pair = list.last(),
                shouldShowIcon = true,
                shouldSetPnlColor = true,
                value = totalPNL,
                percentageChange = percentageChange
            ) {
                showInfoSheet = !showInfoSheet
            }
        }
    }
}

@Preview
@Composable
private fun ProfitLossBottomSheetInfoScreenPreview() {
    ProfitLossBottomSheetInfo(
        modifier = Modifier.background(color = LightGrey),
        detailInfo = InvestmentInfo().apply {
            this.currentValue = 2979507.0
            this.totalInvestment = 2906545.95
            this.todaysPNL = 31841.15
            this.totalPNL = 72961.05
            this.percentageChange = 77.44
        }
    )
}