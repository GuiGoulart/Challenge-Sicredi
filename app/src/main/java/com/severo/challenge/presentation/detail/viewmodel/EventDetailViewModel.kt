package com.severo.challenge.presentation.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.severo.core.usecase.CheckFavoriteUseCase
import com.severo.core.usecase.RemoveFavoriteUseCase
import com.severo.challenge.presentation.detail.FavoriteUiActionStateLiveData
import com.severo.challenge.presentation.detail.viewmodel.EventDetailActionStateLiveData
import com.severo.core.usecase.AddFavoriteUseCase
import com.severo.core.usecase.GetDetailEventsUseCase
import com.severo.core.usecase.GetEventsUseCase
import com.severo.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    getDetailEventsUseCase: GetDetailEventsUseCase,
    coroutinesDispatchers: CoroutinesDispatchers,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    removeFavoriteUseCase: RemoveFavoriteUseCase,
) : ViewModel() {

    val events = EventDetailActionStateLiveData(
        coroutinesDispatchers.main(),
        getDetailEventsUseCase
    )

    val favorite = FavoriteUiActionStateLiveData(
        coroutinesDispatchers.main(),
        checkFavoriteUseCase,
        addFavoriteUseCase,
        removeFavoriteUseCase
    )
}