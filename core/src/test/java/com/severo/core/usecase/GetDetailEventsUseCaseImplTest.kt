package com.severo.core.usecase

import com.nhaarman.mockitokotlin2.whenever
import com.severo.core.data.repository.EventsRepository
import com.severo.core.usecase.base.ResultStatus
import com.severo.testing.MainCoroutineRule
import com.severo.testing.model.EventFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetDetailEventsUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: EventsRepository

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var getDetailEventsUseCase: GetDetailEventsUseCase

    @Before
    fun setUp() {
        getDetailEventsUseCase = GetDetailEventsUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            whenever(repository.getDetailEvent(eventId = event.id)).thenReturn(event)

            val result = getDetailEventsUseCase
                .invoke(GetDetailEventsUseCase.GetDetailEventsParams(eventId = event.id))
            val resultList = result.toList()
            Assert.assertEquals(ResultStatus.Loading, resultList[0])
            Assert.assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
            whenever(repository.getDetailEvent(eventId = event.id)).thenAnswer { throw Throwable() }

            val result = getDetailEventsUseCase
                .invoke(GetDetailEventsUseCase.GetDetailEventsParams(eventId = event.id))

            val resultList = result.toList()
            Assert.assertEquals(ResultStatus.Loading, resultList[0])
            Assert.assertTrue(resultList[1] is ResultStatus.Error)
        }
}