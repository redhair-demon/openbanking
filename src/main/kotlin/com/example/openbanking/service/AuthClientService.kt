package com.example.openbanking.service

import com.example.openbanking.data.ConsentResponseDTO
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Base64

@Service
class AuthClientService(
    private val api: AccountApiClient,
    private val accountClientService: AccountClientService,
    private val tokenService: TokenService,
) {

    @Value("\${oauth2.auth-server.auth-endpoint}")
    private lateinit var authorizationEndpoint: String

    @Value("\${oauth2.client.id}")
    private lateinit var clientId: String

    @Value("\${oauth2.client.redirect-uri}")
    private lateinit var redirectUri: String

    @Value("\${oauth2.auth-server.token-endpoint}")
    private lateinit var tokenEndpoint: String

    @Value("\${oauth2.client.secret}")
    private lateinit var clientSecret: String

    @Value("\${oauth2.resource-server.contacts-endpoint}")
    private lateinit var contactsEndpoint: String


    fun callback(body: Map<String, String>): Boolean {
        tokenService.setToken(body["accessToken"]!!)
        accountClientService.fetchConsent(body)
        return true
    }

    fun generateAuthorizationUrl(): String {
        var authorizationUrl = String.format(
            "%s?client_id=%s&response_type=code&state=%s&redirect_uri=%s&scope=email profile openid accounts.first accounts.second",
            authorizationEndpoint,
            clientId,
            RandomStringUtils.secure().nextAlphabetic(16),
            redirectUri
        )

        val codeVerifier = RandomStringUtils.secure().nextAlphabetic(50)
        try {
            val codeChallenge: String = sha256Hash(codeVerifier)
            logger.info { "$codeChallenge, $codeVerifier" }
            authorizationUrl += "&code_challenge=$codeChallenge"
            authorizationUrl += "&code_challenge_method=S256"
        } catch (e: NoSuchAlgorithmException) {
            logger.warn { "Not using PKCE due to ${e.message}" }
        }

        return authorizationUrl
    }


    private fun sha256Hash(plainText: String?): String {
        val plainBytes = Base64.getUrlDecoder().decode(plainText)
        val messageDigest = MessageDigest.getInstance("SHA256")
        messageDigest.update(plainBytes)
        val digest = messageDigest.digest()
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }
}

private val logger = KotlinLogging.logger {}

