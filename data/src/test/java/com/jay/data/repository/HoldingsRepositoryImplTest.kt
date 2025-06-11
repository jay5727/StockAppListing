package com.jay.data.repository

import com.jay.data.database.DatabaseService
import com.jay.data.database.HoldingEntity
import com.jay.data.network.model.HoldingDto
import com.jay.data.network.model.HoldingResponseModel
import com.jay.data.network.HoldingApiService
import com.jay.domain.repository.HoldingsRepository
import com.jay.presentation.utils.MainDispatcherRule
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class HoldingsRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val apiService: HoldingApiService = mockk()
    private val databaseService: DatabaseService = mockk()
    private lateinit var repository: HoldingsRepository

    @Before
    fun setUp() {
        repository =
            HoldingsRepositoryImpl(apiService, databaseService, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `should emit cached data when available`() = runTest {
        // Arrange
        val cachedEntities = listOf(
            HoldingEntity("ITC", 10, 100.0, 95.0, 98.0)
        )
        coEvery { databaseService.getHoldingList() } returns cachedEntities

        // Act
        val result = repository.getHoldings().first()

        // Assert
        assert(result.isSuccess)
        val data = result.getOrNull()
        assert(data?.first()?.symbol == "ITC")
        coVerify(exactly = 1) { databaseService.getHoldingList() }
        coVerify(exactly = 0) { apiService.getHoldingResponse() } // API should not be called
    }

    @Test
    fun `should fetch from API and cache when no local data`() = runTest {
        // Arrange
        coEvery { databaseService.getHoldingList() } returns emptyList()

        val dto = HoldingDto("ITC", 10, 100.0, 95.0, 98.0)
        val response = HoldingResponseModel(data = HoldingResponseModel.Data(listOf(dto)))

        coEvery { apiService.getHoldingResponse() } returns response
        coEvery { databaseService.insertHoldingList(any()) } just Runs

        // Act
        val result = repository.getHoldings().first()

        // Assert
        assert(result.isSuccess)
        val list = result.getOrNull()
        assert(list?.first()?.symbol == "ITC")

        coVerifySequence {
            databaseService.getHoldingList()
            apiService.getHoldingResponse()
            databaseService.insertHoldingList(match { it.size == 1 })
        }
    }

    @Test
    fun `should emit failure when API call fails`() = runTest {
        // Arrange
        coEvery { databaseService.getHoldingList() } returns emptyList()
        coEvery { apiService.getHoldingResponse() } throws IOException("Network error")

        // Act
        val result = repository.getHoldings().first()

        // Assert
        assert(result.isFailure)
        assert(result.exceptionOrNull() is IOException)

        coVerifySequence {
            databaseService.getHoldingList()
            apiService.getHoldingResponse()
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
