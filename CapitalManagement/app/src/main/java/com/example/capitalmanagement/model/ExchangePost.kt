package com.example.capitalmanagement.model

import java.util.*

data class ExchangePost (
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Rates
)

data class Rates (
    val USD: Double,
    val KZT: Double,
    val RUB: Double,
    val KGS: Double,
    val GBP: Double,
    val CNY: Double
)