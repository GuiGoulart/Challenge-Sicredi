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

interface PostCheckUseCase {

    operator fun invoke(params: PostCheckParams): Flow<ResultStatus<Event>>

    data class PostCheckParams(val eventId: Int)
}

class PostCheckUseCaseImpl @Inject constructor(
    private val repository: EventsRepository,
    private val dispatchers: CoroutinesDispatchers
) : PostCheckUseCase,
    UseCase<PostCheckUseCase.PostCheckParams, Event>() {

    override suspend fun doWork(params: PostCheckUseCase.PostCheckParams): ResultStatus<Event> {
        return withContext(dispatchers.io()) {
            val events = repository.postCheck(params.eventId)
            ResultStatus.Success(events)
        }
    }
}