package com.example.openbanking.rest

import com.example.openbanking.api.BalanceData
import com.example.openbanking.api.DateRange
import com.example.openbanking.service.AccountClientService
import com.example.openbanking.service.AuthClientService
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
@RequestMapping("/auth")
class AuthController(
    private val clientService: AuthClientService
) {
    @PostMapping("/callback")
    fun callback(@RequestBody body: Map<String, String>): Boolean {
        return clientService.callback(body)
    }

    @GetMapping("/auth")
    fun getAuthUrl(): AuthResponse {
        return AuthResponse(clientService.generateAuthorizationUrl())
//        return mapOf("url" to clientService.generateAuthorizationUrl())
    }
}

data class AuthResponse(
    val url: String,
)