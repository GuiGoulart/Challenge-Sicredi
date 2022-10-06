package com.severo.challenge.framework.di

import com.severo.challenge.framework.FavoritesRepositoryImpl
import com.severo.challenge.framework.data.local.RoomFavoritesDataSource
import com.severo.core.data.repository.FavoritesLocalDataSource
import com.severo.core.data.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesRepositoryModule {

    @Binds
    fun bindFavoritesRepository(repository: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindLocalDataSource(
        dataSource: RoomFavoritesDataSource
    ): FavoritesLocalDataSource
}