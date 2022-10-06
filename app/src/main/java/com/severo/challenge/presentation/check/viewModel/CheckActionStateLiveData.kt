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
                    if(it.name.isNotEmpty() && it.email.isNotEmpty()) {
                        postCheckUseCase.invoke(
                            PostCheckUseCase.PostCheckParams(it.eventId, it.name, it.email)
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
                    } else {
                        emit(CheckActionState.Empty(R.string.error_check_empty))
                    }
                }
            }
        }
    }

    fun check(
        eventId: Int,
        name: String,
        email: String
    ) {
        action.value = Action.Check(eventId, name, email)
    }

    sealed class CheckActionState {
        object Loading : CheckActionState()
        data class Success(val detailEvent: Event) : CheckActionState()
        data class Error(@StringRes val messageResId: Int) : CheckActionState()
        data class Empty(@StringRes val messageResId: Int) : CheckActionState()
    }

    sealed class Action {
        data class Check(
            val eventId: Int,
            val name: String,
            val email: String
        ) : Action()
    }
}