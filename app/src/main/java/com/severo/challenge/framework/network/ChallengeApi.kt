package com.severo.challenge.framework.network

import com.severo.challenge.framework.network.request.CheckRequest
import com.severo.challenge.framework.network.response.EventsResponse
import retrofit2.http.*

interface ChallengeApi {

    @GET("events")
    suspend fun getEvents(): List<EventsResponse>

    @GET("events/{eventId}")
    suspend fun getDetailEvent(
        @Path("eventId")
        eventId: Int
    ): EventsResponse

    @POST("checkin")
    suspend fun postCheck(
        @Body checkRequest: CheckRequest
    ): EventsResponse
}