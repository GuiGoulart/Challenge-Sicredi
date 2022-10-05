package com.severo.challenge.presentation.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.severo.challenge.databinding.FragmentEventsBinding
import com.severo.challenge.framework.imageloader.ImageLoader
import com.severo.challenge.presentation.detail.DetailViewArg
import com.severo.challenge.presentation.events.adapter.EventsAdapter
import com.severo.challenge.presentation.events.viewmodel.EventsViewModel
import com.severo.challenge.presentation.events.viewmodel.EventsActionStateLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding: FragmentEventsBinding get() = _binding!!

    private val viewModel: EventsViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val eventsAdapter: EventsAdapter by lazy {
        EventsAdapter(imageLoader) { event, view, title, description ->
            val extras = FragmentNavigatorExtras(
                view to event.image,
                title to event.title,
                description to event.description
            )

            val directions = EventsFragmentDirections
                .actionEventsFragmentToDetailFragment(
                    event.title,
                    DetailViewArg(
                        id = event.id,
                        description = event.description,
                        image = event.image,
                        title = event.title,
                        price = event.price
                    )
                )

            findNavController().navigate(directions, extras)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEventsBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventsAdapter()
        observeInitialLoadState()
    }

    private fun initEventsAdapter() {
        postponeEnterTransition()
        with(binding.recyclerEvents) {
            setHasFixedSize(true)
            adapter = eventsAdapter
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadState() {
        viewModel.events.load()
        viewModel.events.state.observe(viewLifecycleOwner) { uiState ->
            binding.flipperEvents.displayedChild = when (uiState) {
                EventsActionStateLiveData.EventsState.Loading -> {
                    setShimmerVisibility(true)
                    FLIPPER_CHILD_LOADING
                }
                is EventsActionStateLiveData.EventsState.Success -> {
                    setShimmerVisibility(false)
                    eventsAdapter.submitList(uiState.detailParentList)
                    FLIPPER_CHILD_EVENTS
                }
                EventsActionStateLiveData.EventsState.Error -> {
                    binding.includeViewEventsErrorState.buttonRetry.setOnClickListener {
                        viewModel.events.load()
                    }
                    FLIPPER_CHILD_ERROR
                }
                EventsActionStateLiveData.EventsState.Empty -> FLIPPER_CHILD_EMPTY
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewEventsLoadingState.shimmerEvents.run {
            isVisible = visibility
            if (visibility) {
                startShimmer()
            } else stopShimmer()
        }
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_EVENTS = 1
        private const val FLIPPER_CHILD_ERROR = 2
        private const val FLIPPER_CHILD_EMPTY = 3
    }
}