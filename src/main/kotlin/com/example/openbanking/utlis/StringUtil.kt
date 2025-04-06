package com.example.openbanking.utlis

import java.util.UUID

fun String.uuid(): UUID = UUID.fromString(this)