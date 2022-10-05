package com.severo.challenge.presentation.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.severo.challenge.R
import com.severo.challenge.presentation.detail.DetailViewArg
import com.severo.challenge.presentation.detail.FavoriteUiActionStateLiveData
import com.severo.core.usecase.*
import com.severo.core.usecase.base.ResultStatus
import com.severo.testing.MainCoroutineRule
import com.severo.testing.model.EventFactory
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EventsDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getDetailEventsUseCase: GetDetailEventsUseCase

    @Mock
    private lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    @Mock
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<EventDetailActionStateLiveData.EventsDetailState>

    @Mock
    private lateinit var favoriteUiStateObserver: Observer<FavoriteUiActionStateLiveData.UiState>

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var detailViewModel: EventDetailViewModel

    @Before
    fun setUp() {
        detailViewModel = EventDetailViewModel(
            getDetailEventsUseCase,
            mainCoroutineRule.testDispatcherProvider,
            checkFavoriteUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase,

        ).apply {
            events.state.observeForever(uiStateObserver)
            favorite.state.observeForever(favoriteUiStateObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get event returns success`() =
        runTest {
            whenever(getDetailEventsUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            event
                        )
                    )
                )

            detailViewModel.events.load(1)

            verify(uiStateObserver).onChanged(isA<EventDetailActionStateLiveData.EventsDetailState.Success>())

            val uiStateSuccess =
                detailViewModel.events.state.value as EventDetailActionStateLiveData.EventsDetailState.Success
            val categoriesParentList = uiStateSuccess.detailEvent

            assertEquals(event.price, categoriesParentList.price, event.price)
            assertEquals(event.description, categoriesParentList.description)
            assertEquals(event.id, categoriesParentList.id)
            assertEquals(event.title, categoriesParentList.title)
            assertEquals(event.image, categoriesParentList.image)
        }

    @Test
    fun `should notify uiState with Error from UiState when get event returns an exception`() =
        runTest {
            whenever(getDetailEventsUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Error(Throwable())
                    )
                )

            detailViewModel.events.load(1)

            // Assert
            verify(uiStateObserver).onChanged(isA<EventDetailActionStateLiveData.EventsDetailState.Error>())
        }

    @Test
    fun `should notify favorite_uiState with filled favorite icon when check favorite returns true`() =
        runTest {
            // Arrange
            whenever(checkFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(true)
                    )
                )

            // Action
            detailViewModel.favorite.checkFavorite(1)

            // Assert
            verify(favoriteUiStateObserver).onChanged(
                isA<FavoriteUiActionStateLiveData.UiState.Icon>()
            )
            val uiState =
                detailViewModel.favorite.state.value as FavoriteUiActionStateLiveData.UiState.Icon
            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }

    @Test
    fun `should notify favorite_uiState with not filled favorite icon when check favorite returns false`() =
        runTest {
            // Arrange
            whenever(checkFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(false)
                    )
                )

            // Act
            detailViewModel.favorite.checkFavorite(1)

            // Assert
            verify(favoriteUiStateObserver).onChanged(isA<FavoriteUiActionStateLiveData.UiState.Icon>())
            val uiState =
                detailViewModel.favorite.state.value as FavoriteUiActionStateLiveData.UiState.Icon
            assertEquals(R.drawable.ic_favorite_unchecked, uiState.icon)
        }

    @Test
    fun `should notify favorite_uiState with filled favorite icon when current icon is unchecked`() =
        runTest {
            whenever(addFavoriteUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(Unit)
                    )
                )

            detailViewModel.run {
                favorite.currentFavoriteIcon = R.drawable.ic_favorite_unchecked
                favorite.update(
                    DetailViewArg(
                        id = event.id,
                        description = event.description,
                        price = event.price,
                        image = event.image,
                        title = event.title
                    )
                )
            }

            verify(favoriteUiStateObserver).onChanged(isA<FavoriteUiActionStateLiveData.UiState.Icon>())
            val uiState =
                detailViewModel.favorite.state.value as FavoriteUiActionStateLiveData.UiState.Icon
            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }
}