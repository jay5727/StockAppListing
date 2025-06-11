package com.jay.presentation.viewmodel

import com.jay.domain.model.Holding
import com.jay.domain.model.HoldingSummary
import com.jay.domain.model.InvestmentInfo
import com.jay.domain.usecase.GetHoldingsUseCase
import com.jay.presentation.mapper.HoldingUIMapper
import com.jay.presentation.model.HoldingSummaryUI
import com.jay.presentation.model.HoldingUIModel
import com.jay.presentation.state.HoldingScreenUiState
import com.jay.presentation.utils.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class HoldingsViewModelTest {

    private val getHoldingsUseCase: GetHoldingsUseCase = mockk(relaxed = true)
    private val mapper: HoldingUIMapper = mockk(relaxed = true)
    private lateinit var viewModel: HoldingsViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = HoldingsViewModel(getHoldingsUseCase, mapper)
    }

    @Test
    fun `uiState should emit Success when use case returns data`() = runTest {
        // Given

        val holdingList = listOf(Holding("ITC", 10, 100.0, 95.0, 98.0))
        val domain = HoldingSummary(
            holdingsList = holdingList,
            investmentInfo = InvestmentInfo()
        )
        val holdingSummaryUI = HoldingSummaryUI(
            holdingList = listOf(
                HoldingUIModel(
                    holding = domain.holdingsList.first(),
                    individualStockPNL = 5727.0,
                    formattedLTP = "98.0",
                    formattedPNL = "5727.0"
                )
            ),
            investmentInfo = domain.investmentInfo
        )

        every { getHoldingsUseCase() } returns flowOf(Result.success(domain))
        every { mapper.toUIModelList(any()) } returns holdingSummaryUI.holdingList

        val viewModel = HoldingsViewModel(getHoldingsUseCase, mapper)
        // When
        val states = mutableListOf<HoldingScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        advanceUntilIdle()

        // Then
        assert(states.first() is HoldingScreenUiState.Loading)
        assert(states.last() is HoldingScreenUiState.Success)
        assertEquals(holdingSummaryUI, (states.last() as HoldingScreenUiState.Success).summary)

        job.cancel()
    }

    @Test
    fun `uiState should emit Error when use case returns failure`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        every { getHoldingsUseCase() } returns flowOf(Result.failure(exception))

        viewModel = HoldingsViewModel(getHoldingsUseCase, mapper)

        // When
        val states = mutableListOf<HoldingScreenUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.toList(states)
        }

        advanceUntilIdle()

        // Then
        assert(states.first() is HoldingScreenUiState.Loading)
        assert(states.last() is HoldingScreenUiState.Error)
        assertEquals("Network error", (states.last() as HoldingScreenUiState.Error).message)

        job.cancel()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}