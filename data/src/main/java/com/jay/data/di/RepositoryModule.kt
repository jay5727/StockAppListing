package com.jay.data.di

import com.jay.data.network.HoldingApiService
import com.jay.data.repository.HoldingsRepositoryImpl
import com.jay.domain.repository.HoldingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideHoldingRepository(repository: HoldingsRepositoryImpl): HoldingsRepository

}