package com.severo.core.usecase

import com.severo.core.data.repository.FavoritesRepository
import com.severo.core.domain.model.Event
import com.severo.core.usecase.base.CoroutinesDispatchers
import com.severo.core.usecase.base.ResultStatus
import com.severo.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AddFavoriteUseCase {

    operator fun invoke(params: Params): Flow<ResultStatus<Unit>>

    data class Params(
        val description: String,
        val imageUrl: String,
        val price: Double,
        val title: String,
        val idEvent: Int
    )
}

class AddFavoriteUseCaseImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : UseCase<AddFavoriteUseCase.Params, Unit>(), AddFavoriteUseCase {

    override suspend fun doWork(params: AddFavoriteUseCase.Params): ResultStatus<Unit> {
        return withContext(dispatchers.io()) {
            favoritesRepository.saveFavorite(
                Event(
                    params.description,
                    params.imageUrl,
                    params.price,
                    params.title,
                    params.idEvent
                )
            )
            ResultStatus.Success(Unit)
        }
    }
}