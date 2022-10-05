package com.severo.challenge.presentation.favorites

import com.severo.challenge.framework.common.ListItem

data class FavoriteItem(
    val description: String,
    val image: String,
    val price: Double,
    val title: String,
    val id: Int,
    override val key: Long = id.toLong()
) : ListItem
