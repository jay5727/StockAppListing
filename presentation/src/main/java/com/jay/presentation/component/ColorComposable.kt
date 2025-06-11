package com.jay.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jay.presentation.theme.GreenC
import com.jay.presentation.theme.RedC

@Composable
fun ColorComposable(value: Double): Color {
    return if (value > 0) {
        GreenC
    } else {
        RedC
    }
}