package com.severo.challenge.presentation.events.viewmodel

import androidx.lifecycle.ViewModel
import com.severo.core.usecase.GetDetailEventsUseCase
import com.severo.core.usecase.GetEventsUseCase
import com.severo.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    getEventsUseCase: GetEventsUseCase,
    coroutinesDispatchers: CoroutinesDispatchers,
) : ViewModel() {

    val events = EventsActionStateLiveData(
        coroutinesDispatchers.main(),
        getEventsUseCase
    )
}