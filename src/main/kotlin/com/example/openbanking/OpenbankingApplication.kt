package com.example.openbanking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class OpenbankingApplication

fun main(args: Array<String>) {
    runApplication<OpenbankingApplication>(*args)
}
