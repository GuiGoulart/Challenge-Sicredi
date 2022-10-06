package com.severo.challenge.util

import android.content.Context
import android.content.Intent
import com.severo.challenge.R

fun Context.sharingEvent(title: String, description: String, price: Double) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            getString(R.string.sharing_event, title, description, price.priceFormatCurrency())
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}