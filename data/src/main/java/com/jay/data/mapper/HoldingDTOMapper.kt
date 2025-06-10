package com.jay.data.mapper

import com.jay.data.model.HoldingDto
import com.jay.domain.model.Holding

/**
 * DTO - Domain Model
 */
fun HoldingDto.toDomain() = Holding(
    this.symbol,
    this.quantity,
    this.ltp,
    this.avgPrice,
    this.close
)
