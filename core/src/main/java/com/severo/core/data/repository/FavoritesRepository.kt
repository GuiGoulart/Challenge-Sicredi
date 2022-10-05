package com.severo.core.data.repository

import com.severo.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getAll(): Flow<List<Event>>

    suspend fun isFavorite(eventId: Int): Boolean

    suspend fun saveFavorite(event: Event)

    suspend fun deleteFavorite(event: Event)
}