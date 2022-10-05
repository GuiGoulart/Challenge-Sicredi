package com.severo.challenge.framework.di

import com.severo.challenge.framework.EventsRepositoryImpl
import com.severo.challenge.framework.remote.RetrofitEventsDataSource
import com.severo.core.data.repository.EventsRemoteDataSource
import com.severo.core.data.repository.EventsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EventsRepositoryModule {

    @Binds
    fun bindEventsRepository(repository: EventsRepositoryImpl): EventsRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitEventsDataSource
    ): EventsRemoteDataSource
}