package com.example.openbanking.rest

import com.example.openbanking.api.ValidateRequest
import com.example.openbanking.dao.User
import com.example.openbanking.dao.UserDAO
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(
    val userDAO: UserDAO
) {
    @PostMapping("/validate")
    fun validate(@RequestBody request: ValidateRequest): Boolean {
        logger.info { "Start validation for $request" }
        val passwordHash = UUID.nameUUIDFromBytes(request.password.toByteArray())
        val hash = get(request.phone)?.passwordHash
        logger.info { "End validation with ${hash == passwordHash}" }
        return hash == passwordHash
    }

    @GetMapping("/{phone}")
    fun get(@PathVariable phone: String): User? {
        logger.info { "Get user for $phone" }
        return userDAO.findByPhone(phone)
    }
}

private val logger = KotlinLogging.logger {}
