package com.severo.core.usecase

import com.nhaarman.mockitokotlin2.whenever
import com.severo.core.data.repository.EventsRepository
import com.severo.core.usecase.base.ResultStatus
import com.severo.testing.MainCoroutineRule
import com.severo.testing.model.EventFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetEventsUseCaseImplTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: EventsRepository

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var getEventsUseCase: GetEventsUseCase

    @Before
    fun setUp() {
        getEventsUseCase = GetEventsUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            whenever(repository.getEvents()).thenReturn(listOf(event))

            val result = getEventsUseCase
                .invoke(GetEventsUseCase.GetEventsParams)
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
            whenever(repository.getEvents()).thenAnswer { throw Throwable() }

            val result = getEventsUseCase
                .invoke(GetEventsUseCase.GetEventsParams)

            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }
}