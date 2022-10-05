package com.severo.challenge.presentation.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.severo.challenge.databinding.FragmentCheckBinding
import com.severo.challenge.presentation.check.viewModel.CheckActionStateLiveData
import com.severo.challenge.presentation.check.viewModel.CheckViewModel
import com.severo.challenge.presentation.detail.DetailFragment
import com.severo.challenge.presentation.detail.DetailFragmentArgs
import com.severo.challenge.presentation.detail.DetailViewArg
import com.severo.challenge.presentation.detail.viewmodel.EventDetailViewModel
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
            check(idEvent)

//            binding.imageFavoriteIcon.setOnClickListener {
//                update(detailViewArg)
//            }

            state.observe(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    CheckActionStateLiveData.CheckActionState.Loading -> {}
                    is CheckActionStateLiveData.CheckActionState.Success -> {}
                    is CheckActionStateLiveData.CheckActionState.Error -> {
                        showShortToast(uiState.messageResId)
                    }
                }
            }
        }
    }
}