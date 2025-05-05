package com.example.openbanking.api

import java.time.Instant

data class ValidateRequest (
    val phone: String,
    val password: String,
)

data class DateRange (
    val from: String,
    val to: String,
)