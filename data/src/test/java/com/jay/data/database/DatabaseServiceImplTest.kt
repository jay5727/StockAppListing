package com.jay.data.database

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DatabaseServiceImplTest {

    private val dao: HoldingDao = mockk(relaxed = true)
    private lateinit var dbService: DatabaseService

    @Before
    fun setup() {
        dbService = DatabaseServiceImpl(dao)
    }

    @Test
    fun `insertHoldingList should call DAO insert`() = runTest {
        val holdings = listOf(HoldingEntity("ITC", 10, 100.0, 95.0, 98.0))

        dbService.insertHoldingList(holdings)

        coVerify { dao.insertHoldingList(holdings) }
    }

    @Test
    fun `getHoldingList should return data from DAO`() = runTest {
        val expected = listOf(HoldingEntity("ITC", 10, 100.0, 95.0, 98.0))
        coEvery { dao.getHoldingList() } returns expected

        val result = dbService.getHoldingList()

        assert(result == expected)
        coVerify { dao.getHoldingList() }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }
}
