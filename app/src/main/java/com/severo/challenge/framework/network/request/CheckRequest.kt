package com.severo.challenge.framework.network.request

import com.severo.core.domain.model.Check

data class CheckRequest(
    val eventId: Int,
    val name: String,
    val email: String
)

fun Check.toCheckRequest() = CheckRequest(
    eventId = this.eventId,
    name = this.name,
    email = this.email
)
