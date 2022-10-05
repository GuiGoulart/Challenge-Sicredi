package com.severo.core.usecase

import com.severo.core.data.repository.FavoritesRepository
import com.severo.core.domain.model.Event
import com.severo.core.usecase.base.CoroutinesDispatchers
import com.severo.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<List<Event>>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : FlowUseCase<Unit, List<Event>>(), GetFavoritesUseCase {

    override suspend fun createFlowObservable(params: Unit): Flow<List<Event>> {
        return withContext(dispatchers.io()) {
            favoritesRepository.getAll()
        }
    }
}