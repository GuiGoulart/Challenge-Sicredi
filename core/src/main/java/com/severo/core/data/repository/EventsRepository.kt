package com.severo.core.data.repository

import com.severo.core.domain.model.Event

interface EventsRepository {

    suspend fun getEvents(): List<Event>

    suspend fun getDetailEvent(eventId: Int): Event
}