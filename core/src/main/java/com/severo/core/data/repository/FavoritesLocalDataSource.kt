package com.severo.core.data.repository

import com.severo.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {

    fun getAll(): Flow<List<Event>>

    suspend fun isFavorite(eventId: Int): Boolean

    suspend fun save(event: Event)

    suspend fun delete(event: Event)
}