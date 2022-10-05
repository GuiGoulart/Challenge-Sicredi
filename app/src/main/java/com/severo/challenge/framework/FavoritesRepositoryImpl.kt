package com.severo.challenge.framework

import com.severo.core.data.repository.FavoritesLocalDataSource
import com.severo.core.data.repository.FavoritesRepository
import com.severo.core.domain.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
) : FavoritesRepository {

    override fun getAll(): Flow<List<Event>> {
        return favoritesLocalDataSource.getAll()
    }

    override suspend fun isFavorite(eventId: Int): Boolean {
        return favoritesLocalDataSource.isFavorite(eventId)
    }

    override suspend fun saveFavorite(event: Event) {
        favoritesLocalDataSource.save(event)
    }

    override suspend fun deleteFavorite(event: Event) {
        favoritesLocalDataSource.delete(event)
    }
}