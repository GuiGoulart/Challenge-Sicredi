package com.severo.challenge.framework.data.network.response

import com.google.gson.annotations.SerializedName
import com.severo.core.domain.model.Event

data class EventsResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("title")
    val title: String,
    @SerializedName("id")
    val id: Int
)

fun EventsResponse.toEventModel() = Event(
    description = this.description,
    image = this.image,
    price = this.price,
    title = this.title,
    id = this.id
)