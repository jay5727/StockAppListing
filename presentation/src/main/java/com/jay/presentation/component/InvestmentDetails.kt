package com.jay.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jay.domain.util.StringConstants.CurrentValue
import com.jay.domain.util.getFormattedString
import com.jay.presentation.model.InvestmentDataItem

@Composable
fun InvestmentDetails(
    modifier: Modifier = Modifier,
    item: InvestmentDataItem,
    shouldShowIcon: Boolean = false,
    onPnLClicked: () -> Unit = {}
) {
    var rotateIcon by remember {
        mutableFloatStateOf(0F)
    }
    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically
    ) {
        Text(text = item.label)
        if (shouldShowIcon) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(rotationZ = rotateIcon)
                    .clickable {
                        rotateIcon = if (rotateIcon == 0F) 180F else 0F
                        onPnLClicked()
                    },
                imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        val textWithColors = mutableListOf(
            Pair(
                first = item.value,
                second = if (item.showPnlColor) ColorComposable(value = item.amount) else Color.Black
            )
        )
        if (shouldShowIcon) {
            textWithColors.add(
                Pair(
                    first = buildString {
                        append("(")
                        append(item.percentageChange.toString())
                        append("%)")
                    },
                    second = if (item.showPnlColor) ColorComposable(value = item.amount) else Color.Black
                )
            )
        }
        MultiColorText(*textWithColors.toTypedArray())
    }
}

@Preview
@Composable
private fun InvestmentDetailsPreview() {
    InvestmentDetails(
        modifier = Modifier.fillMaxWidth(),
        item = InvestmentDataItem(CurrentValue, 5727.0.getFormattedString()),
        onPnLClicked = {}
    )
}