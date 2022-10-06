package com.severo.challenge.presentation.check

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.severo.challenge.R
import com.severo.challenge.databinding.FragmentCheckBinding
import com.severo.challenge.presentation.check.viewModel.CheckActionStateLiveData
import com.severo.challenge.presentation.check.viewModel.CheckViewModel
import com.severo.challenge.presentation.detail.DetailFragment
import com.severo.challenge.presentation.detail.viewmodel.FavoriteUiActionStateLiveData
import com.severo.challenge.util.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCheckBinding? = null
    private val binding: FragmentCheckBinding get() = _binding!!

    private val viewModel: CheckViewModel by viewModels()

    private val args by navArgs<CheckFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCheckBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idEvent = args.eventId
        setAndObserveCheckUiState(idEvent)
    }

    private fun setAndObserveCheckUiState(idEvent: Int) {
        viewModel.check.run {

            binding.buttonCheckIn.setOnClickListener {
                check(idEvent, binding.editName.text.toString(), binding.editEmail.text.toString())
            }

            state.observe(viewLifecycleOwner) { uiState ->
                binding.flipperCheck.displayedChild = when (uiState) {
                    CheckActionStateLiveData.CheckActionState.Loading -> {
                        FLIPPER_CHILD_POSITION_LOADING
                    }
                    is CheckActionStateLiveData.CheckActionState.Success -> {
                        this@CheckFragment.dismiss()
                        FLIPPER_CHILD_POSITION_SUCCESS
                    }
                    is CheckActionStateLiveData.CheckActionState.Error -> {
                        showShortToast(uiState.messageResId)
                        this@CheckFragment.dismiss()
                        FLIPPER_CHILD_POSITION_ERROR
                    }
                    is CheckActionStateLiveData.CheckActionState.Empty -> {
                        showShortToast(uiState.messageResId)
                        FLIPPER_CHILD_POSITION_ERROR
                    }
                }
            }
        }
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 1
        private const val FLIPPER_CHILD_POSITION_SUCCESS = 0
        private const val FLIPPER_CHILD_POSITION_ERROR = 0
    }
}