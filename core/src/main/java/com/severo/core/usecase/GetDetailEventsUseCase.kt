package com.severo.core.usecase

import com.severo.core.data.repository.EventsRepository
import com.severo.core.domain.model.Event
import com.severo.core.usecase.base.CoroutinesDispatchers
import com.severo.core.usecase.base.ResultStatus
import com.severo.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetDetailEventsUseCase {

    operator fun invoke(params: GetDetailEventsParams): Flow<ResultStatus<Event>>

    data class GetDetailEventsParams(val eventId: Int)
}

class GetDetailEventsUseCaseImpl @Inject constructor(
    private val repository: EventsRepository,
    private val dispatchers: CoroutinesDispatchers
) : GetDetailEventsUseCase,
    UseCase<GetDetailEventsUseCase.GetDetailEventsParams, Event>() {

    override suspend fun doWork(params: GetDetailEventsUseCase.GetDetailEventsParams): ResultStatus<Event> {
        return withContext(dispatchers.io()) {
            val events = repository.getDetailEvent(params.eventId)
            ResultStatus.Success(events)
        }
    }
}