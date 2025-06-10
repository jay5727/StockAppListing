package com.jay.stockapplisting.di

import com.jay.domain.mapper.HoldingSummaryMapper
import com.jay.domain.repository.HoldingsRepository
import com.jay.domain.usecase.GetHoldingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideHoldingSummaryMapper(): HoldingSummaryMapper = HoldingSummaryMapper()

    @Provides
    fun provideGetHoldingsUseCase(
        repository: HoldingsRepository,
        mapper: HoldingSummaryMapper
    ): GetHoldingsUseCase {
        return GetHoldingsUseCase(repository, mapper)
    }
}
