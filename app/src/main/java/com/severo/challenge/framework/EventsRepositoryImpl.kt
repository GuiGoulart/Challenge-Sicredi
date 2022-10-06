package com.severo.challenge.framework

import com.severo.challenge.framework.remote.RetrofitEventsDataSource
import com.severo.core.data.repository.EventsRepository
import com.severo.core.domain.model.Check
import com.severo.core.domain.model.Event
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RetrofitEventsDataSource,
) : EventsRepository {
    override suspend fun getEvents(): List<Event> {
        return remoteDataSource.fetchEvents()
    }

    override suspend fun getDetailEvent(eventId: Int): Event {
        return remoteDataSource.fetchDetailEvents(eventId)
    }

    override suspend fun postCheck(eventId: Int, name: String, email: String): Event {
        return remoteDataSource.sendCheckEvent(
            Check(
                eventId = eventId,
                name = name,
                email = email
            )
        )
    }
}