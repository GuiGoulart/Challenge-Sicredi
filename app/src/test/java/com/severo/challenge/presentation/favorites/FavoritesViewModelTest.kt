package com.severo.challenge.presentation.favorites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.severo.core.usecase.GetFavoritesUseCase
import com.severo.testing.MainCoroutineRule
import com.severo.testing.model.EventFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class FavoritesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<FavoritesViewModel.UiState>

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var favoritesViewModel: FavoritesViewModel

    @Before
    fun setUp() {
        favoritesViewModel = FavoritesViewModel(
            getFavoritesUseCase,
            mainCoroutineRule.testDispatcherProvider
        )
        favoritesViewModel.state.observeForever(uiStateObserver)

    }

    @Test
    fun `should notify uiState with Success from UiState when get event returns success`() =
        runTest {
            whenever(getFavoritesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        listOf(event)
                    )
                )

            favoritesViewModel.getAll()

            verify(uiStateObserver).onChanged(isA<FavoritesViewModel.UiState.ShowFavorite>())

            val uiStateSuccess =
                favoritesViewModel.state.value as FavoritesViewModel.UiState.ShowFavorite
            val categoriesParentList = uiStateSuccess.favorites

            assertEquals(1, categoriesParentList.size)
        }

    @Test
    fun `should notify ui State with Empty from Ui State when get event returns empty`() =
        runTest {
            whenever(getFavoritesUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        emptyList()
                    )
                )

            favoritesViewModel.getAll()

            verify(uiStateObserver).onChanged(isA<FavoritesViewModel.UiState.ShowEmpty>())
        }
}