package com.example.openbanking.rest

import com.example.openbanking.api.ValidateRequest
import com.example.openbanking.dao.User
import com.example.openbanking.dao.UserDAO
import com.example.openbanking.service.Chart
import com.example.openbanking.service.ChartService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/charts")
class ChartController(
    val chartService: ChartService
) {
    @PostMapping
    fun store(@RequestBody request: Chart) {
        chartService.storeChart(request)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): Chart? {
        return chartService.getChart(id)
    }

    @GetMapping
    fun getAll(): List<Chart> {
        return chartService.getAllCharts()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) {
        chartService.deleteChart(id)
    }
}

private val logger = KotlinLogging.logger {}
