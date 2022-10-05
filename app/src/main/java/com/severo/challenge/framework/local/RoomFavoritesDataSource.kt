package com.severo.challenge.framework.local

import com.severo.challenge.framework.db.dao.FavoriteDao
import com.severo.challenge.framework.db.entity.FavoriteEntity
import com.severo.challenge.framework.db.entity.toEventsModel
import com.severo.core.data.repository.FavoritesLocalDataSource
import com.severo.core.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoritesLocalDataSource {

    override fun getAll(): Flow<List<Event>> {
        return favoriteDao.loadFavorites().map {
            it.toEventsModel()
        }
    }

    override suspend fun isFavorite(eventId: Int): Boolean {
        return favoriteDao.hasFavorite(eventId) != null
    }

    override suspend fun save(event: Event) {
        favoriteDao.insertFavorite(event.toFavoriteEntity())
    }

    override suspend fun delete(event: Event) {
        favoriteDao.deleteFavorite(event.toFavoriteEntity())
    }

    private fun Event.toFavoriteEntity() = FavoriteEntity(
        id = id,
        title = title,
        price = price,
        image = image,
        description = description
    )
}