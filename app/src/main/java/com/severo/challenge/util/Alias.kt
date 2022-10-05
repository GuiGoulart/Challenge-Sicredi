package com.severo.challenge.util

import android.widget.ImageView
import android.widget.TextView
import com.severo.core.domain.model.Event

typealias OnEventItemClick<T> = (event: T, image: ImageView, textTitle: TextView, textDescription: TextView) -> Unit