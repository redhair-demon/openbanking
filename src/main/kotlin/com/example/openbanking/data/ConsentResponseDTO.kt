package com.example.openbanking.data

import java.time.OffsetDateTime

// DTOs

data class ConsentResponseDTO(
    val Data: ConsentDataDTO,
    val Links: LinksDTO?,
    val Meta: MetaDTO?
)

data class ConsentDataDTO(
    val consentId: String,
    val userId: String,
    val accountIds: List<String>,
    val creationDateTime: OffsetDateTime,
    val status: String,
    val statusUpdateDateTime: OffsetDateTime,
    val permissions: List<String>,
    val expirationDateTime: OffsetDateTime,
    val transactionFromDateTime: OffsetDateTime?,
    val transactionToDateTime: OffsetDateTime?
)

//data class Consent(
//    val consentId: String,
//    val userId: String,
//    val creationDateTime: OffsetDateTime,
//    val status: String,
//    val expirationDateTime: OffsetDateTime,
//    val transactionFromDateTime: OffsetDateTime?,
//    val transactionToDateTime: OffsetDateTime?,
//)

data class AccountsResponseDTO(
    val Data: AccountsDataDTO,
    val Links: LinksDTO?,
    val Meta: MetaDTO?
)

data class AccountsDataDTO(
    val Account: List<AccountDTO>
)

data class AccountDTO(
    val accountId: String,
    val currency: String,
    val accountType: String,
    val accountSubType: String?,
    val accountDescription: String?,
)

data class BalancesResponseDTO(
    val Data: BalancesDataDTO,
    val Links: LinksDTO?,
    val Meta: MetaDTO?
)

data class BalancesDataDTO(
    val Balance: List<BalanceDTO>
)

data class BalanceDTO(
    val accountId: String?,
    val type: String,
    val amount: AmountDTO
)

data class AmountDTO(
    val amount: String,
    val currency: String
)

data class TransactionsResponseDTO(
    val Data: TransactionsDataDTO,
    val Links: LinksDTO?,
    val Meta: MetaDTO?
)

data class TransactionsDataDTO(
    val Transaction: List<TransactionDTO>
)

data class TransactionDTO(
    val transactionId: String,
    val bookingDateTime: OffsetDateTime?,
    val amount: AmountDTO,
    val creditDebitIndicator: String,
    val transactionInformation: String?
)

data class RetrievalGrantResponseDTO(
    val Data: RetrievalGrantDataDTO,
    val Links: LinksDTO?,
    val Meta: MetaDTO?
)

data class RetrievalGrantDataDTO(
    val retrievalGrant: String
)

data class LinksDTO(
    val self: String
)

data class MetaDTO(
    val totalPages: Int?
)
