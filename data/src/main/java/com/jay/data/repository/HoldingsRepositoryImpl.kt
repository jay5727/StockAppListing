package com.jay.data.repository

import android.util.Log
import com.jay.data.database.DatabaseService
import com.jay.data.di.IoDispatcher
import com.jay.data.mapper.toDomain
import com.jay.data.mapper.toEntity
import com.jay.data.network.HoldingApiService
import com.jay.domain.model.Holding
import com.jay.domain.repository.HoldingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: HoldingApiService,
    private val databaseService: DatabaseService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HoldingsRepository {


    private val hardcodedRetrofit = Retrofit.Builder()
        .baseUrl("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        val cached = databaseService.getHoldingList()


        val shouldFetchFromNetwork = cached.isEmpty() || shouldRefreshData()

        if (!shouldFetchFromNetwork) {
            //DB Entity -> Domain
            emit(Result.success(cached.map { it.toDomain() }))

            Log.d("Repository", "Returning cached data: ${cached.size} items")
        } else {
            try {

                val response = if (Math.random() > 0.5) {
                    apiService.getHoldingResponse()
                } else {

                    hardcodedRetrofit.create(HoldingApiService::class.java).getHoldingResponse()
                }

                //DTO-> Domain
                val domainList = response.data.userHolding.map { it.toDomain() }

                val transformedList = transformData(domainList)

                //Domain -> DB Entity
                databaseService.insertHoldingList(transformedList.map { it.toEntity() })

                emit(Result.success(transformedList))

                Log.d("Repository", "Network fetch successful: ${transformedList.size} items")

            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    emit(Result.failure(Throwable("No Internet Connection")))
                    Log.e("Repository", "Network error occurred")
                } else {
                    emit(Result.failure(e))
                    Log.e("Repository", "Unknown error: ${e.message}")
                }
            }
        }
    }.flowOn(ioDispatcher)

    private fun shouldRefreshData(): Boolean {
        return System.currentTimeMillis() % 300000 == 0L
    }

    private fun transformData(holdings: List<Holding>): List<Holding> {
        return holdings.filter { it.quantity > 0 }
    }
}



