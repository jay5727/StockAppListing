package com.jay.data.di

import com.jay.data.database.DatabaseService
import com.jay.data.database.DatabaseServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DatabaseServiceModule {

    @Binds
    @Singleton
    fun bindDatabaseService(impl: DatabaseServiceImpl): DatabaseService
}