package com.severo.challenge.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.severo.challenge.R
import com.severo.challenge.databinding.FragmentDetailBinding
import com.severo.challenge.framework.imageloader.ImageLoader
import com.severo.challenge.presentation.detail.viewmodel.EventDetailActionStateLiveData
import com.severo.challenge.presentation.detail.viewmodel.EventDetailViewModel
import com.severo.challenge.util.dateFormat
import com.severo.challenge.util.extensions.showShortToast
import com.severo.challenge.util.priceFormatCurrency
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: EventDetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArg = args.detailViewArg
        binding.imageEvent.run {
            transitionName = detailViewArg.image
            imageLoader.load(this, detailViewArg.image)
        }

        setSharedElementTransitionOnEnter()

        loadCategoriesAndObserveUiState(detailViewArg)
        setAndObserveFavoriteUiState(detailViewArg)
    }

    private fun loadCategoriesAndObserveUiState(detailViewArg: DetailViewArg) {
        viewModel.events.load(detailViewArg.id)
        viewModel.events.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperDetail.displayedChild = when (uiState) {
                EventDetailActionStateLiveData.EventsDetailState.Loading -> FLIPPER_CHILD_POSITION_LOADING
                is EventDetailActionStateLiveData.EventsDetailState.Success -> {
                    binding.includeViewDetailSuccessState.textTitle.text = uiState.detailEvent.title
                    binding.includeViewDetailSuccessState.textDescription.text = context?.resources?.getString(
                        R.string.description_event, uiState.detailEvent.description
                    )
                    binding.includeViewDetailSuccessState.textPrice.text = context?.resources?.getString(
                        R.string.price_event, uiState.detailEvent.price.priceFormatCurrency()
                    )

                    FLIPPER_CHILD_POSITION_DETAIL
                }
                EventDetailActionStateLiveData.EventsDetailState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
                        viewModel.events.load(detailViewArg.id)
                    }
                    FLIPPER_CHILD_POSITION_ERROR
                }
                EventDetailActionStateLiveData.EventsDetailState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
            }
        }
    }

    private fun setAndObserveFavoriteUiState(detailViewArg: DetailViewArg) {
        viewModel.favorite.run {
            checkFavorite(detailViewArg.id)

            binding.imageFavoriteIcon.setOnClickListener {
                update(detailViewArg)
            }

            state.observe(viewLifecycleOwner) { uiState ->
                binding.flipperFavorite.displayedChild = when (uiState) {
                    FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                    is FavoriteUiActionStateLiveData.UiState.Icon -> {
                        binding.imageFavoriteIcon.setImageResource(uiState.icon)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                    is FavoriteUiActionStateLiveData.UiState.Error -> {
                        showShortToast(uiState.messageResId)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                }
            }
        }
    }

    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3
        private const val FLIPPER_FAVORITE_CHILD_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }
}