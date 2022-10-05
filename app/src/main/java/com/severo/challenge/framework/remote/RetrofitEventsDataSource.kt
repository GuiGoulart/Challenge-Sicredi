package com.severo.challenge.framework.remote

import com.severo.challenge.framework.network.ChallengeApi
import com.severo.challenge.framework.network.request.toCheckRequest
import com.severo.challenge.framework.network.response.toEventModel
import com.severo.core.data.repository.EventsRemoteDataSource
import com.severo.core.domain.model.Check
import com.severo.core.domain.model.Event
import javax.inject.Inject

class RetrofitEventsDataSource @Inject constructor(
    private val challengeApi: ChallengeApi
) : EventsRemoteDataSource {

    override suspend fun fetchEvents(): List<Event> {
        return challengeApi.getEvents().map {
            it.toEventModel()
        }
    }

    override suspend fun fetchDetailEvents(eventId: Int): Event {
        return challengeApi.getDetailEvent(eventId).toEventModel()
    }

    override suspend fun sendCheckEvent(check: Check): Event {
        return challengeApi.postCheck(check.toCheckRequest()).toEventModel()
    }
}