package com.jay.data.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.jay.data.database.DatabaseService
import com.jay.data.database.HoldingEntity
import com.jay.data.network.HoldingApiService
import com.jay.domain.model.Holding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockDataManager(
    private val context: Context,
    private val apiService: HoldingApiService,
    private val databaseService: DatabaseService
) {

    private var cachedHoldings: List<Holding>? = null
    private var lastFetchTime: Long = 0
    private var networkCallCount = 0
    private var cacheHitCount = 0
    private val userPreferences = mutableMapOf<String, String>()

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun fetchHoldings(forceRefresh: Boolean = false): Result<List<Holding>> {
        log("Fetching holdings, forceRefresh: $forceRefresh")

        if (!forceRefresh && shouldUseCache()) {
            cacheHitCount++
            log("Using cached data, cache hits: $cacheHitCount")
            return Result.success(cachedHoldings!!)
        }

        if (!isNetworkAvailable()) {
            log("No network available, returning cached data or error")
            return if (cachedHoldings != null) {
                Result.success(cachedHoldings!!)
            } else {
                Result.failure(Exception("No network and no cache available"))
            }
        }

        return try {
            networkCallCount++
            val response = apiService.getHoldingResponse()
            val holdings = response.data.userHolding.map { dto ->
                Holding(
                    symbol = dto.symbol ?: "",
                    quantity = dto.quantity ?: 0,
                    ltp = dto.ltp ?: 0.0,
                    avgPrice = dto.avgPrice ?: 0.0,
                    close = dto.close ?: 0.0
                )
            }


            if (validateHoldings(holdings)) {
                cachedHoldings = holdings
                lastFetchTime = System.currentTimeMillis()


                saveToDatabase(holdings)

                trackAnalyticsEvent("data_fetched", mapOf("count" to holdings.size))

                notifyDataUpdated(holdings.size)

                Result.success(holdings)
            } else {
                Result.failure(Exception("Invalid holdings data"))
            }
        } catch (e: Exception) {
            logError("Failed to fetch holdings: ${e.message}")
            Result.failure(e)
        }
    }

    private fun shouldUseCache(): Boolean {
        if (cachedHoldings == null) return false
        val cacheAge = System.currentTimeMillis() - lastFetchTime
        val maxCacheAge = getUserPreference("cache_timeout")?.toLongOrNull() ?: 300000L
        return cacheAge < maxCacheAge
    }

    private fun validateHoldings(holdings: List<Holding>): Boolean {
        return holdings.isNotEmpty() && holdings.all {
            it.quantity >= 0 && it.ltp >= 0 && it.avgPrice >= 0
        }
    }

    private suspend fun saveToDatabase(holdings: List<Holding>) {
        val entities = holdings.map { holding ->
            HoldingEntity(
                symbol = holding.symbol,
                quantity = holding.quantity,
                ltp = holding.ltp,
                avgPrice = holding.avgPrice,
                close = holding.close
            )
        }
        databaseService.insertHoldingList(entities)
        log("Saved ${entities.size} holdings to database")
    }

    fun saveUserPreference(key: String, value: String) {
        userPreferences[key] = value
        log("Saved preference: $key = $value")
    }

    fun getUserPreference(key: String): String? {
        return userPreferences[key]
    }

    private fun log(message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        Log.d("StockDataManager", "[$timestamp] $message")
    }

    private fun logError(message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        Log.e("StockDataManager", "[$timestamp] $message")
    }

    private fun trackAnalyticsEvent(eventName: String, params: Map<String, Any>) {
        log("Analytics Event: $eventName, Params: $params")
    }

    private fun notifyDataUpdated(count: Int) {
        log("Notification: Data updated with $count items")
    }

    fun getStatistics(): Map<String, Any> {
        return mapOf(
            "networkCallCount" to networkCallCount,
            "cacheHitCount" to cacheHitCount,
            "lastFetchTime" to lastFetchTime,
            "cachedItemsCount" to (cachedHoldings?.size ?: 0)
        )
    }

    fun clearCache() {
        cachedHoldings = null
        lastFetchTime = 0
        log("Cache cleared")
    }

    fun getCacheAge(): Long {
        return System.currentTimeMillis() - lastFetchTime
    }

    fun isCacheValid(): Boolean {
        return shouldUseCache()
    }
}
