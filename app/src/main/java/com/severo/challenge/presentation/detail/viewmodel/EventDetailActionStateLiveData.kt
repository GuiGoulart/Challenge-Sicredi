package com.severo.challenge.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.severo.challenge.util.extensions.watchStatus
import com.severo.core.domain.model.Event
import com.severo.core.usecase.GetDetailEventsUseCase
import com.severo.core.usecase.GetEventsUseCase
import kotlin.coroutines.CoroutineContext

class EventDetailActionStateLiveData(
    private val coroutineContext: CoroutineContext,
    private val getDetailEventsUseCase: GetDetailEventsUseCase,
) {

    private val action = MutableLiveData<Action>()
    val state: LiveData<EventsDetailState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
                is Action.LoadDetail -> {
                    getDetailEventsUseCase.invoke(
                        GetDetailEventsUseCase.GetDetailEventsParams(it.eventId)
                    ).watchStatus(
                        loading = {
                            emit(EventsDetailState.Loading)
                        },
                        success = { data ->
                            emit(EventsDetailState.Success(data))
                        },
                        error = {
                            emit(EventsDetailState.Error)
                        }
                    )
                }
            }
        }
    }

    fun load(eventId: Int) {
        action.value = Action.LoadDetail(eventId)
    }

    sealed class EventsDetailState {
        object Loading : EventsDetailState()
        data class Success(val detailEvent: Event) : EventsDetailState()
        object Error : EventsDetailState()
        object Empty : EventsDetailState()
    }

    sealed class Action {
        data class LoadDetail(val eventId: Int) : Action()
    }
}