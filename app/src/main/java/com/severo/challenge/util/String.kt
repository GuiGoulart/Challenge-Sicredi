package com.severo.challenge.util

fun String.urlReplaceHttpToHttps(): String {
    return if (this.contains("https")) this else
        this.replace("http", "https")
}