package com.jay.data.di

import android.content.Context
import androidx.room.Room
import com.jay.data.database.HoldingDao

import com.jay.data.database.HoldingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HoldingDatabase {
        return Room.databaseBuilder(
            context,
            HoldingDatabase::class.java,
            "holding_database"
        ).build()
    }

    @Provides
    fun provideHoldingDao(database: HoldingDatabase): HoldingDao {
        return database.holdingDao()
    }
}