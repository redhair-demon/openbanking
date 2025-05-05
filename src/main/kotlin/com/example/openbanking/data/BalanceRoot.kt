package com.example.openbanking.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class BalanceRoot(
    @JsonProperty("Data")
    val data: Data,
) {
    data class Data(
        @JsonProperty("Balance")
        val account: List<Balance>,
    )
}

data class Balance(
    val accountId: String,
    val type: String,
    @field:JsonProperty("Amount") val amount: Amount,
    val creditDebitIndicator: String,
    val dateTime: String,
    @field:JsonProperty("CreditLine") val creditLine: List<CreditLine>
) {
    data class Amount(val amount: BigDecimal, val currency: String)

    data class CreditLine(val included: Boolean, @field:JsonProperty("Amount") val amount: Amount)
}

