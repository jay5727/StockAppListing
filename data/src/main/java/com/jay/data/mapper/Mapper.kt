package com.jay.data.mapper

import com.jay.data.database.HoldingEntity
import com.jay.data.network.model.HoldingDto
import com.jay.domain.model.Holding

fun HoldingDto.toDomain(): Holding = Holding(
    symbol = this.symbol,
    quantity = this.quantity,
    ltp = this.ltp,
    avgPrice = this.avgPrice,
    close = this.close
)

fun Holding.toEntity(): HoldingEntity = HoldingEntity(
    symbol = this.symbol,
    quantity = this.quantity,
    ltp = this.ltp,
    avgPrice = this.avgPrice,
    close = this.close
)

fun HoldingEntity.toDomain(): Holding = Holding(
    symbol = this.symbol,
    quantity = this.quantity,
    ltp = this.ltp,
    avgPrice = this.avgPrice,
    close = this.close
)

