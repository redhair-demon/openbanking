package com.example.openbanking.service

import org.springframework.stereotype.Component

@Component
class TokenService {
    private var token: String = ""

    fun getToken() = "Bearer $token"

    fun setToken(token: String) { this.token = token }
}