package com.example.openbanking.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.Instant
import java.time.OffsetDateTime

class TransactionRoot (
    @JsonProperty("Data")
    val data: Data,
    ) {
        data class Data(
            @JsonProperty("Transaction")
            val transaction: List<Transaction>,
        )
    }

data class Transaction(
    val transactionId: String,
    val accountId: String,
    @field:JsonProperty(
        "Amount"
    ) val amount: Amount,
    val creditDebitIndicator: String,
    val status: String,
    @field:JsonProperty("BankTransactionCode") val bankTransactionCode: BankTransactionCode,
    val bookingDateTime: OffsetDateTime,
    val valueDateTime: OffsetDateTime,
    @field:JsonProperty("ChargeAmount") val chargeAmount: Amount,
    @field:JsonProperty("InstructedAmount") val instructedAmount: Amount,
    @field:JsonProperty(
        "DebtorAccount"
    ) val debtorAccount: Account,
    @field:JsonProperty("Creditor") val creditor: Account,
    @field:JsonProperty(
        "CardTransaction"
    ) val cardTransaction: CardTransaction,
    @field:JsonProperty("RemittanceInformation") val remittanceInformation: RemittanceInformation,
    val finalBalance: BigDecimal
) {
    @JsonProperty("CreditorAccount")
    private val creditorAccount: Account? = null

    @JsonProperty("Debtor")
    private val debtor: Account? = null


    data class Amount(val amount: BigDecimal, val currency: String)

    data class BankTransactionCode(val code: String, val subCode: String)

    data class Account(
        val name: String,
        @field:JsonProperty("Identification") val identification: List<Identification>,
        @field:JsonProperty("PostalAddress") val postalAddress: PostalAddress?,
        @field:JsonProperty("MerchantInformation") val merchantInformation: MerchantInformation?
    )

    data class Identification(val schemeName: String, val identification: String)

    data class PostalAddress(val townName: String, val country: String)

    data class MerchantInformation(val merchantId: String, val merchantCategoryCode: String)

    data class CardTransaction(
        @field:JsonProperty("Card") val card: Card, @field:JsonProperty(
            "Transaction"
        ) val transaction: Transaction2
    )

    data class Card(
        val cardScheme: String, val additionalCardData: String, @field:JsonProperty(
            "PaymentCardData"
        ) val paymentCardData: PaymentCardData
    )

    data class PaymentCardData(@field:JsonProperty("PAN") val pan: String, val expiryDate: String)

    data class Transaction2(val authorizationCode: String)

    data class RemittanceInformation(val unstructured: String)
}