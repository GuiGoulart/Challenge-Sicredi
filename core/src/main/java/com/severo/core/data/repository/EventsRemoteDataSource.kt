package com.severo.core.data.repository

import com.severo.core.domain.model.Check
import com.severo.core.domain.model.Event

interface EventsRemoteDataSource {

    suspend fun fetchEvents(): List<Event>

    suspend fun fetchDetailEvents(eventId: Int): Event

    suspend fun sendCheckEvent(check: Check): Event
}