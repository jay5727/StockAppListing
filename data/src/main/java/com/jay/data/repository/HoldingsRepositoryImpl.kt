package com.jay.data.repository

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
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HoldingsRepositoryImpl @Inject constructor(
    private val apiService: HoldingApiService,
    private val databaseService: DatabaseService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : HoldingsRepository {

    override fun getHoldings(): Flow<Result<List<Holding>>> = flow {
        val cached = databaseService.getHoldingList()

        if (cached.isNotEmpty()) {
            //DB Entity -> Domain
            emit(Result.success(cached.map { it.toDomain() }))
        } else {
            try {
                val response = apiService.getHoldingResponse()
                //DTO-> Domain
                val domainList = response.data.userHolding.map { it.toDomain() }


                //Domain -> DB Entity
                databaseService.insertHoldingList(domainList.map { it.toEntity() })

                emit(Result.success(domainList))
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    emit(Result.failure(Throwable("No Internet Connection")))
                } else {
                    emit(Result.failure(e))
                }
            }
        }
    }.flowOn(ioDispatcher)
}


