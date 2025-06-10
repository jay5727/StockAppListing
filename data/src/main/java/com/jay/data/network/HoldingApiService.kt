package com.jay.data.network

import com.jay.data.model.HoldingResponseModel
import retrofit2.http.GET

/**
 * API Service for Holdings
 */
interface HoldingApiService {
    @GET("/")
    suspend fun getHoldingResponse(): HoldingResponseModel
}
