package com.severo.challenge.presentation.events.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.severo.core.usecase.*
import com.severo.core.usecase.base.ResultStatus
import com.severo.testing.MainCoroutineRule
import com.severo.testing.model.EventFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EventsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getEventsUseCase: GetEventsUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<EventsActionStateLiveData.EventsState>

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var eventsViewModel: EventsViewModel

    @Before
    fun setUp() {
        eventsViewModel = EventsViewModel(
            getEventsUseCase,
            mainCoroutineRule.testDispatcherProvider

            ).apply {
            events.state.observeForever(uiStateObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get event returns success`() =
        runTest {
            whenever(getEventsUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            listOf(event)
                        )
                    )
                )

            eventsViewModel.events.load()

            verify(uiStateObserver).onChanged(isA<EventsActionStateLiveData.EventsState.Success>())

            val uiStateSuccess =
                eventsViewModel.events.state.value as EventsActionStateLiveData.EventsState.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            Assert.assertEquals(1, categoriesParentList.size)
        }

    @Test
    fun `should notify ui State with Empty from Ui State when get event returns empty`() =
        runTest {
            whenever(getEventsUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            emptyList()
                        )
                    )
                )

            eventsViewModel.events.load()

            verify(uiStateObserver).onChanged(isA<EventsActionStateLiveData.EventsState.Empty>())
        }

    @Test
    fun `should notify uiState with Error from UiState when get event returns an exception`() =
        runTest {
            whenever(getEventsUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Error(Throwable())
                    )
                )

            eventsViewModel.events.load()

            verify(uiStateObserver).onChanged(isA<EventsActionStateLiveData.EventsState.Error>())
        }

}