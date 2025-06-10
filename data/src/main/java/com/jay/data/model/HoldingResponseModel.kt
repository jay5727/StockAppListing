package com.jay.data.model

import com.google.gson.annotations.SerializedName

data class HoldingResponseModel(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("userHolding")
        val userHolding: List<HoldingDto>
    )
}