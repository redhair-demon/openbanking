package com.example.openbanking.service

import com.example.openbanking.data.Account
import com.example.openbanking.data.AccountsResponseDTO
import com.example.openbanking.data.BalancesResponseDTO
import com.example.openbanking.data.ConsentResponseDTO
import com.example.openbanking.data.RetrievalGrantResponseDTO
import com.example.openbanking.data.AccountRoot
import com.example.openbanking.data.BalanceRoot
import com.example.openbanking.data.TransactionRoot
import com.example.openbanking.data.TransactionsResponseDTO
import feign.HeaderMap
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.time.Instant

@FeignClient(name = "account-api", url = "\${account.api.base-url:http://localhost:8082}")
interface AccountApiClient {

    @PostMapping("/account-consents")
    fun createConsent(@RequestBody body: Map<String, Any>): ConsentResponseDTO

    @GetMapping("/account-consents/{consentId}")
    fun getConsent(@RequestHeader("Authorization") token: String, @PathVariable consentId: String): ConsentResponseDTO

    @DeleteMapping("/account-consents/{consentId}")
    fun deleteConsent(@RequestHeader("Authorization") token: String, @PathVariable consentId: String)

    @GetMapping("/account-consents/{consentId}/retrieval-grant")
    fun getRetrievalGrant(@PathVariable consentId: String): RetrievalGrantResponseDTO

    @RequestMapping("/accounts", method = [RequestMethod.GET])
    fun getAccounts(@RequestHeader("Authorization") token: String): AccountRoot

    @GetMapping("/accounts/{accountId}")
    fun getAccountById(@RequestHeader("Authorization") token: String, @PathVariable accountId: String): AccountRoot

    @GetMapping("/accounts/{accountId}/balances")
    fun getAccountBalances(@RequestHeader("Authorization") token: String, @PathVariable accountId: String): BalanceRoot

    @GetMapping("/accounts/{accountId}/transactions?from={from}&to={to}")
    fun getAccountTransactions(
        @RequestHeader("Authorization") token: String,
        @PathVariable accountId: String,
        @RequestParam("from") from: Instant? = null,
        @RequestParam("to") to: Instant? = null,
    ): TransactionRoot
}
