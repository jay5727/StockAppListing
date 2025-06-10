package com.jay.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jay.presentation.screen.HoldingScreen
import com.jay.presentation.theme.StockAppListingTheme
import com.jay.presentation.viewmodel.HoldingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockAppListingTheme {
                val viewModel = hiltViewModel<HoldingsViewModel>()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                HoldingScreen(screenState = state) {
                    viewModel.fetchHoldings()
                }
            }
        }
    }
}