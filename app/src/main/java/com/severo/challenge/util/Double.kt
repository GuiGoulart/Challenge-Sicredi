package com.severo.challenge.util

import java.text.SimpleDateFormat
import java.util.*

fun Double.priceFormatCurrency(): String {
    val locale = Locale("pt", "BR")
    val currency: Currency = Currency.getInstance(locale)
    val symbol: String = currency.symbol
    return "$symbol$this"
}

fun Long.dateFormat(): String {
    val myLong = System.currentTimeMillis() + (this * 1000)
    val itemDate = Date(myLong)
    return SimpleDateFormat("dd/MM/yyyy").format(itemDate)
}