package com.example.openbanking.api

import java.awt.Color
import java.time.Instant
import java.time.OffsetDateTime
import java.util.UUID

data class BalanceData(
    val date: Long,
    val amount: Long,
)

data class AccountData(
    val id: UUID,
    val type: String,
    val name: String,
    val bank: Bank,
    var isFavorite: Boolean = false,
    val balance: Long? = null,
) {
    enum class Type {
        CREDIT, DEBIT
    }
}

data class Bank(
    val name: String,
    val bik: String?,
    val imageUrl: String?,
    val color: Color?,
)

data class Transaction(
    val id: UUID,
    val amount: Long,
    val isCredit: Boolean,
    val time: OffsetDateTime,
    val debtor: Account,
    val creditor: Account,
    val information: String,
) {
    data class Account(
        val name: String,
        val id: String,
    )
}