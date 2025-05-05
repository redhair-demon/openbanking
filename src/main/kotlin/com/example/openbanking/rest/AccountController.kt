package com.example.openbanking.rest

import com.example.openbanking.api.BalanceData
import com.example.openbanking.api.DateRange
import com.example.openbanking.service.AccountClientService
import com.example.openbanking.service.AuthClientService
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/client")
class AccountController(
    private val clientService: AccountClientService
) {

    @PostMapping("/account-consents")
    fun createConsent(@RequestBody body: Map<String, Any>) = clientService.createConsent(body)

    @GetMapping("/account-consents")
    fun getUserConsents(@RequestParam userId: String) = clientService.getUserConsents(userId)

    @GetMapping("/account-consents/{consentId}")
    fun getConsent(@PathVariable consentId: String) = clientService.getConsent(consentId)

    @DeleteMapping("/account-consents/{consentId}")
    fun deleteConsent(@PathVariable consentId: String) = clientService.deleteConsent(consentId)

    @GetMapping("/account-consents/{consentId}/retrieval-grant")
    fun getRetrievalGrant(@PathVariable consentId: String) = clientService.getRetrievalGrant(consentId)

    @GetMapping("/accounts")
    fun getAccounts() = clientService.getAccounts()

    @GetMapping("/accounts-with-balance")
    fun getAccountsWithBalance() = clientService.getAccountsWithBalance()

    @GetMapping("/accounts/{accountId}")
    fun getAccountById(@PathVariable accountId: String) = clientService.getAccountById(accountId)

    @GetMapping("/accounts/{accountId}/balances")
    fun getAccountBalances(@PathVariable accountId: String) = clientService.getAccountBalances(accountId)

    @GetMapping("/accounts/{accountId}/transactions")
    fun getAccountTransactions(
        @PathVariable accountId: String,
        @RequestParam("from") from: Instant,
        @RequestParam("to") to: Instant,
    ) = clientService.getAccountTransactions(accountId, from, to)

//    @GetMapping("/accounts/{accountId}/transactions")
//    fun getAccountTransactions(
//        @PathVariable accountId: String,
//        @RequestParam limit: Int,
//        @RequestParam offset: Int,
//    ) = clientService.getAccountTransactions(accountId, limit, offset)

    @PostMapping("/accounts/{accountId}/balances")
    fun getBalanceListById(@PathVariable accountId: String, @RequestBody range: DateRange): List<BalanceData> {
        return clientService.getBalanceListById(accountId, Instant.parse(range.from), Instant.parse(range.to))
    }
}