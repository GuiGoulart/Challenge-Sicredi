package com.severo.challenge.presentation.check.viewModel

import androidx.lifecycle.ViewModel
import com.severo.core.usecase.*
import com.severo.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckViewModel @Inject constructor(
    postCheckUseCase: PostCheckUseCase,
    coroutinesDispatchers: CoroutinesDispatchers,
) : ViewModel() {

    val check = CheckActionStateLiveData(
        postCheckUseCase,
        coroutinesDispatchers.main(),
    )
}