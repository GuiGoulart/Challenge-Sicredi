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

interface GetEventsUseCase {

    operator fun invoke(params: GetEventsParams): Flow<ResultStatus<List<Event>>>

    object GetEventsParams
}

class GetEventsUseCaseImpl @Inject constructor(
    private val repository: EventsRepository,
    private val dispatchers: CoroutinesDispatchers
) : GetEventsUseCase,
    UseCase<GetEventsUseCase.GetEventsParams, List<Event>>() {

    override suspend fun doWork(params: GetEventsUseCase.GetEventsParams): ResultStatus<List<Event>> {
        return withContext(dispatchers.io()) {
            val events = repository.getEvents()
            ResultStatus.Success(events)
        }
    }
}