package com.severo.challenge.util

import java.text.SimpleDateFormat
import java.util.*

fun Double.priceFormatCurrency(): String {
    val locale = Locale("pt", "BR")
    val currency: Currency = Currency.getInstance(locale)
    val symbol: String = currency.symbol
    return "$symbol$this"
}