package com.severo.challenge.presentation.detail

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DetailViewArg(
    val id: Int,
    val description: String,
    val price: Double,
    val image: String,
    val title: String,
) : Parcelable
