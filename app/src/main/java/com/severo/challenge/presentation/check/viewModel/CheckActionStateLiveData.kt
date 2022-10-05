package com.severo.challenge.presentation.check.viewModel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.severo.challenge.R
import com.severo.challenge.util.extensions.watchStatus
import com.severo.core.domain.model.Event
import com.severo.core.usecase.PostCheckUseCase
import kotlin.coroutines.CoroutineContext

class CheckActionStateLiveData(
    private val postCheckUseCase: PostCheckUseCase,
    private val coroutineContext: CoroutineContext
) {

    private val action = MutableLiveData<Action>()
    val state: LiveData<CheckActionState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
                is Action.Check -> {
                    postCheckUseCase.invoke(
                        PostCheckUseCase.PostCheckParams(it.eventId)
                    ).watchStatus(
                        loading = {
                            emit(CheckActionState.Loading)
                        },
                        success = { data ->
                            emit(CheckActionState.Success(data))
                        },
                        error = {
                            emit(CheckActionState.Error(R.string.error_check))
                        }
                    )
                }
            }
        }
    }

    fun check(eventId: Int) {
        action.value = Action.Check(eventId)
    }

    sealed class CheckActionState {
        object Loading : CheckActionState()
        data class Success(val detailEvent: Event) : CheckActionState()
        data class Error(@StringRes val messageResId: Int) : CheckActionState()
    }

    sealed class Action {
        data class Check(val eventId: Int) : Action()
    }
}