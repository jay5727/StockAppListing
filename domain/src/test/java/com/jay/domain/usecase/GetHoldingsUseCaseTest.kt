package com.jay.domain.usecase

import com.jay.domain.mapper.HoldingSummaryMapper
import com.jay.domain.model.Holding
import com.jay.domain.model.HoldingSummary
import com.jay.domain.model.InvestmentInfo
import com.jay.domain.repository.HoldingsRepository
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetHoldingsUseCaseTest {

    private val repository: HoldingsRepository = mockk()
    private val mapper: HoldingSummaryMapper = mockk()
    private lateinit var useCase: GetHoldingsUseCase

    @Before
    fun setUp() {
        useCase = GetHoldingsUseCase(repository, mapper)
    }

    @Test
    fun `invoke should return mapped HoldingSummary when repository returns success`() = runTest {
        // Arrange
        val holdings = listOf(
            Holding("ITC", 10, 100.0, 95.0, 98.0)
        )
        val summary = HoldingSummary(holdingsList = holdings, investmentInfo = InvestmentInfo())

        every { repository.getHoldings() } returns flowOf(Result.success(holdings))
        every { mapper.map(holdings) } returns summary

        // Act
        val result = useCase().first()

        // Assert
        assert(result.isSuccess)
        assertEquals(summary, result.getOrNull())

        verifySequence {
            repository.getHoldings()
            mapper.map(holdings)
        }
    }

    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        every { repository.getHoldings() } returns flowOf(Result.failure(exception))

        // Act
        val result = useCase().first()

        // Assert
        assert(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)

        verify { repository.getHoldings() }
        confirmVerified(mapper)
    }

    @Test
    fun `invoke should return failure when mapper throws exception`() = runTest {
        // Arrange
        val holdings = listOf(
            Holding("ITC", 10, 100.0, 95.0, 98.0)
        )
        val mappingError = IllegalStateException("Mapper crashed")

        every { repository.getHoldings() } returns flowOf(Result.success(holdings))
        every { mapper.map(holdings) } throws mappingError

        // Act
        val result = useCase().first()

        // Assert
        assert(result.isFailure)
        assertEquals(mappingError, result.exceptionOrNull())

        verifySequence {
            repository.getHoldings()
            mapper.map(holdings)
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
