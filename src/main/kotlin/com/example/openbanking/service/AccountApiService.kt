package com.example.openbanking.service

import com.example.openbanking.api.AccountData
import com.example.openbanking.api.BalanceData
import com.example.openbanking.api.Bank
import com.example.openbanking.data.Account
import com.example.openbanking.data.BalanceRoot
import com.example.openbanking.data.ConsentDataDTO
import com.example.openbanking.data.ConsentResponseDTO
import com.example.openbanking.data.RetrievalGrantResponseDTO
import com.example.openbanking.data.Transaction
import com.example.openbanking.data.TransactionRoot
import com.example.openbanking.utlis.uuid
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import java.util.Base64
import java.util.UUID
import com.example.openbanking.api.Transaction as TransactionApi

@Service
class AccountClientService(
    private val api: AccountApiClient,
    private val tokenService: TokenService,
) {
    val consents: MutableMap<String, ConsentDataDTO> = mutableMapOf()

    fun fetchConsent(body: Map<String, String>) {
        api.getConsent(tokenService.getToken(), body["consentId"]!!).Data.let {
            consents.put(it.consentId, it)
        }
    }

    fun createConsent(body: Map<String, Any>): ConsentDataDTO {
        return api.createConsent(body).Data.let {
            consents.put(it.consentId, it)!!
        }
    }

    fun getUserConsents(userId: String): List<ConsentDataDTO> {
        return consents.mapNotNull { it.value.takeIf { it.userId == userId } }
    }

    fun getConsent(consentId: String): ConsentDataDTO? = consents[consentId]

    fun deleteConsent(consentId: String) {
        try {
            api.deleteConsent(tokenService.getToken(), consentId)
            consents.remove(consentId)
        } catch (e: Exception) {
            logger.warn { "Error deleting consent for $consentId: ${e.message}" }
        }
    }

    fun getRetrievalGrant(consentId: String): RetrievalGrantResponseDTO = api.getRetrievalGrant(consentId)

    fun getAccounts(): List<AccountData> {
        try {
            val response = api.getAccounts(tokenService.getToken())
            val result = response.data.account.map {
                it.toAccountData()
            }
            logger.info { "result: $result" }
            return result
        } catch (e: Exception) {
            logger.warn { "Error: ${e.message}" }
            return listOf()
        }
    }
    fun getAccountsWithBalance(): List<AccountData> {
        try {
            val accounts = api.getAccounts(tokenService.getToken())
            return accounts.data.account.map {
                it.toAccountData(api.getAccountBalances(tokenService.getToken(), it.accountId).data.account.first().amount.amount)
            }
        } catch (e: Exception) {
            logger.warn { "Error: ${e.message}" }
            return listOf()
        }
    }

    fun getAccountById(accountId: String): AccountData? = try {
        api.getAccountById(tokenService.getToken(), accountId).data.account.first()
            .toAccountData(
                api.getAccountBalances(
                    tokenService.getToken(),
                    accountId
                ).data.account.first().amount.amount
            )
    } catch (e: Exception) {
        logger.warn { "Error: ${e.message}" }
        null
    }

    fun getAccountBalances(accountId: String): BalanceRoot = api.getAccountBalances(tokenService.getToken(), accountId)

    fun getBalanceListById(id: String, from: Instant, to: Instant): List<BalanceData> {
        val result = mutableMapOf<Long, BalanceData>()
        val fromEpochDay = LocalDate.ofInstant(from, UTC).toEpochDay()
        val toEpochDay = LocalDate.ofInstant(to, UTC).toEpochDay()
        var lastEpochDay = fromEpochDay
        var lastTransaction: Transaction? = null
        api.getAccountTransactions(tokenService.getToken(), id, null, null).data.transaction.reversed().forEach { transaction ->
            if (result.containsKey(toEpochDay)) return@forEach
//            if (transaction.valueDateTime.toInstant() < from)
            if (transaction.valueDateTime.toInstant() >= from) {
                val txEpochDay = transaction.valueDateTime.toLocalDate().toEpochDay()
                result[txEpochDay] = BalanceData(txEpochDay, (transaction.finalBalance * BigDecimal(100)).toLong())
                lastTransaction?.let { lastTransaction ->
                    if (txEpochDay - lastEpochDay > 1)
                        result.putAll(List((txEpochDay - lastEpochDay).toInt()) {
                            lastEpochDay + it to result.getOrPut(lastEpochDay) {
                                BalanceData(
                                    lastEpochDay,
                                    (lastTransaction.finalBalance * BigDecimal(100)).toLong()
                                )
                            }.copy(date = lastEpochDay + it)
                        })
                }
                lastEpochDay = txEpochDay
                lastTransaction = transaction
            }
        }
        if (!result.containsKey(toEpochDay) && toEpochDay - lastEpochDay > 0 ) {
            result.putAll(List((toEpochDay - lastEpochDay + 1).toInt()) {
                lastEpochDay + it to result[lastEpochDay]!!.copy(date = lastEpochDay + it)
            })
        }

        return result.values.toList().filter { it.date in fromEpochDay..toEpochDay }.sortedBy { it.date }.mapNotNull { result[it.date] }
    }

    fun getBalanceListByIdV2(id: String, from: Instant, to: Instant): List<BalanceData> {
        val result = mutableMapOf<Long, BalanceData>()
        val fromEpochDay = LocalDate.ofInstant(from, UTC).toEpochDay()
        val toEpochDay = LocalDate.ofInstant(to, UTC).toEpochDay()
        var lastEpochDay = fromEpochDay
        var lastTransaction: Transaction? = null
        val accountTransactions = api.getAccountTransactions(tokenService.getToken(), id, null, null).data.transaction
        accountTransactions.reversed().forEach { transaction ->
            if (result.containsKey(toEpochDay)) return@forEach
            if (transaction.valueDateTime.toInstant() < from) lastTransaction = transaction
            else {
                val txEpochDay = transaction.valueDateTime.toLocalDate().toEpochDay()
                result[txEpochDay] = BalanceData(txEpochDay, (transaction.finalBalance * BigDecimal(100)).toLong())
                lastTransaction?.let { lastTransaction ->
                    result.putAll(List((txEpochDay - lastEpochDay).toInt()) {
                        lastEpochDay + it to result.getOrPut(lastEpochDay) {
                            BalanceData(
                                lastEpochDay,
                                (lastTransaction.finalBalance * BigDecimal(100)).toLong()
                            )
                        }.copy(date = lastEpochDay + it)
                    })
                }
                lastEpochDay = txEpochDay
            }
        }
        if (!result.containsKey(toEpochDay)) {
            result.putAll(List((toEpochDay - lastEpochDay + 1).toInt()) {
                lastEpochDay + it to result[lastEpochDay]!!.copy(date = lastEpochDay + it)
            })
        }

        return result.values.toList().filter { it.date in fromEpochDay..toEpochDay }.sortedBy { it.date }.mapNotNull { result[it.date] }
    }

    fun getAccountTransactions(accountId: String, from: Instant? = null, to: Instant? = null): List<TransactionApi> =
        api.getAccountTransactions(tokenService.getToken(), accountId, from, to).data.transaction.map {
            TransactionApi(
                id = it.transactionId.uuid(),
                amount = (it.amount.amount * BigDecimal(100)).toLong(),
                isCredit = it.creditDebitIndicator == "Credit",
                time = it.valueDateTime,
                debtor = TransactionApi.Account(
                    name = it.debtorAccount.name,
                    id = it.debtorAccount.identification.first().identification
                ),
                creditor = TransactionApi.Account(
                    name = it.creditor.name,
                    id = it.creditor.identification.first().identification
                ),
                information = it.remittanceInformation.unstructured
            )
        }

//    fun getAccountTransactions(accountId: String, limit: Int, offset: Int): List<TransactionApi> =
//        api.getAccountTransactions(tokenService.getToken(), accountId, null, null).data.transaction.subList(offset, offset + limit).map {
//            TransactionApi(
//                id = it.transactionId.uuid(),
//                amount = it.amount.amount.toInt(),
//                isCredit = it.creditDebitIndicator == "Credit",
//                time = it.valueDateTime,
//                debtor = TransactionApi.Account(
//                    name = it.debtorAccount.name,
//                    id = it.debtorAccount.identification.first().identification
//                ),
//                creditor = TransactionApi.Account(
//                    name = it.creditor.name,
//                    id = it.creditor.identification.first().identification
//                ),
//                information = it.remittanceInformation.unstructured
//            )
//        }

    private fun Account.toAccountData(balance: BigDecimal? = null) = AccountData(
        id = UUID.fromString(accountId),
        type = accountType,
        name = accountDetails[0].name,
        bank = servicer.let {
            Bank(
                name = it.name,
                bik = it.bankIdentification.find { it.schemeName == "RU.CBR.BIC" }?.identification,
                imageUrl = null,
                color = null
            )
        },
        isFavorite = false,
        balance = balance?.let { (it * BigDecimal(100)).toLong() }
    )
}

private val logger = KotlinLogging.logger {}
