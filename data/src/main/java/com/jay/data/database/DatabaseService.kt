package com.jay.data.database

interface DatabaseService {

    // Original methods
    suspend fun insertHoldingList(countries: List<HoldingEntity>)
    suspend fun getHoldingList(): List<HoldingEntity>

    suspend fun saveUserPreference(key: String, value: String)
    suspend fun getUserPreference(key: String): String?
    suspend fun clearUserPreferences()


    suspend fun logAnalyticsEvent(eventName: String, params: Map<String, Any>)
    suspend fun getAnalyticsHistory(): List<String>

    suspend fun clearCache()
    suspend fun getCacheSize(): Long
    suspend fun isCacheValid(): Boolean

    suspend fun scheduleNotification(message: String, delayMillis: Long)
    suspend fun cancelAllNotifications()

    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    suspend fun isUserAuthenticated(): Boolean
    suspend fun logout()

    suspend fun exportDataToFile(filePath: String): Boolean
    suspend fun importDataFromFile(filePath: String): Boolean

    suspend fun searchHoldings(query: String): List<HoldingEntity>
    suspend fun getHoldingsBySymbol(symbol: String): HoldingEntity?

    suspend fun calculateTotalValue(): Double
    suspend fun getTopPerformers(count: Int): List<HoldingEntity>
    suspend fun getWorstPerformers(count: Int): List<HoldingEntity>
}

