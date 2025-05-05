package com.example.openbanking.service

import kotlinx.serialization.Serializable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class ChartService {
    val charts: MutableMap<String, Chart> = mutableMapOf()

    fun storeChart(chart: Chart) {
        charts[chart.id] = chart
    }

    fun getChart(id: String): Chart? = charts[id]

    fun getAllCharts(): List<Chart> = charts.values.toList()

    fun deleteChart(id: String) {
        charts.remove(id)
    }
}

@Serializable
data class Chart(
    val id: String,
    val name: String,
    @Serializable
    val dateRange: DateRange,
    val picked: List<String>,
)

@Serializable
data class DateRange(
    val from: Long,
    val to: Long,
)