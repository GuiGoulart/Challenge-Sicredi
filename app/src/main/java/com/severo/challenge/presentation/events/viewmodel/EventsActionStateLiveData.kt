package com.severo.challenge.presentation.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.severo.challenge.util.extensions.watchStatus
import com.severo.core.domain.model.Event
import com.severo.core.usecase.GetEventsUseCase
import kotlin.coroutines.CoroutineContext

class EventsActionStateLiveData(
    private val coroutineContext: CoroutineContext,
    private val getEventsUseCase: GetEventsUseCase,
) {

    private val action = MutableLiveData<Action>()
    val state: LiveData<EventsState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
                is Action.LoadList -> {
                    getEventsUseCase.invoke(
                        GetEventsUseCase.GetEventsParams
                    ).watchStatus(
                        loading = {
                            emit(EventsState.Loading)
                        },
                        success = { data ->
                            var eventParentList = listOf<Event>()

                            if (data.isNotEmpty()) {
                                data.map {
                                    Event(
                                        description = it.description,
                                        image = it.image,
                                        price = it.price,
                                        title = it.title,
                                        id = it.id
                                    )
                                }.also {
                                    eventParentList = it
                                }
                            }

                            if (eventParentList.isNotEmpty()) {
                                emit(EventsState.Success(eventParentList))
                            } else emit(EventsState.Empty)
                        },
                        error = {
                            emit(EventsState.Error)
                        }
                    )
                }
            }
        }
    }

    fun load() {
        action.value = Action.LoadList
    }

    sealed class EventsState {
        object Loading : EventsState()
        data class Success(val detailParentList: List<Event>) : EventsState()
        object Error : EventsState()
        object Empty : EventsState()
    }

    sealed class Action {
        object LoadList : Action()
    }
}