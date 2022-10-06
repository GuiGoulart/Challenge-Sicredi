package com.severo.challenge.presentation.check.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.severo.challenge.R
import com.severo.challenge.presentation.detail.viewmodel.EventDetailActionStateLiveData
import com.severo.core.usecase.PostCheckUseCase
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
class CheckViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var postCheckUseCase: PostCheckUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<CheckActionStateLiveData.CheckActionState>

    private val event = EventFactory().create(EventFactory.Events.ADOCAO)

    private lateinit var checkViewModel: CheckViewModel

    @Before
    fun setUp() {
        checkViewModel = CheckViewModel(
            postCheckUseCase,
            mainCoroutineRule.testDispatcherProvider
            ).apply {
            check.state.observeForever(uiStateObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get event returns success`() =
        runTest {
            whenever(postCheckUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            event
                        )
                    )
                )

            checkViewModel.check.check(event.id, "Name", "E-mail")

            verify(uiStateObserver).onChanged(isA<CheckActionStateLiveData.CheckActionState.Success>())

            val uiState = checkViewModel.check.state.value as CheckActionStateLiveData.CheckActionState.Success
            val result = uiState.detailEvent

            Assert.assertEquals(event.price, result.price, event.price)
            Assert.assertEquals(event.description, result.description)
            Assert.assertEquals(event.id, result.id)
            Assert.assertEquals(event.title, result.title)
            Assert.assertEquals(event.image, result.image)
        }

    @Test
    fun `should notify uiState with Empty when the user passes the name and or email empty`() =
        runTest {
            whenever(postCheckUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(
                            event
                        )
                    )
                )

            checkViewModel.check.check(event.id, "", "")

            verify(uiStateObserver).onChanged(isA<CheckActionStateLiveData.CheckActionState.Empty>())

            val uiState = checkViewModel.check.state.value as CheckActionStateLiveData.CheckActionState.Empty
            val result = uiState.messageResId
            Assert.assertEquals(R.string.error_check_empty, result)
        }

    @Test
    fun `should notify uiState with Error from UiState when get event returns an exception`() =
        runTest {
            whenever(postCheckUseCase.invoke(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Error(Throwable())
                    )
                )

            checkViewModel.check.check(event.id, "Name", "E-mail")

            // Assert
            verify(uiStateObserver).onChanged(isA<CheckActionStateLiveData.CheckActionState.Error>())

            val uiState = checkViewModel.check.state.value as CheckActionStateLiveData.CheckActionState.Error
            val result = uiState.messageResId
            Assert.assertEquals(R.string.error_check, result)
        }
}