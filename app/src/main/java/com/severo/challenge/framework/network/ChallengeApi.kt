package com.severo.challenge.framework.network

import com.severo.challenge.framework.network.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ChallengeApi {

    @GET("events")
    suspend fun getEvents(): List<EventsResponse>

    @GET("events/{eventId}")
    suspend fun getDetailEvent(
        @Path("eventId")
        eventId: Int
    ): EventsResponse
}