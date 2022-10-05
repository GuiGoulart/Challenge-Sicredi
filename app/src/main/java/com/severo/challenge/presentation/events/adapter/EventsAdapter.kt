package com.severo.challenge.presentation.events.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.severo.challenge.framework.imageloader.ImageLoader
import com.severo.challenge.util.OnEventItemClick
import com.severo.core.domain.model.Event
import javax.inject.Inject

class EventsAdapter @Inject constructor(
    private val imageLoader: ImageLoader,
    private val onItemClick: OnEventItemClick<Event>
) : ListAdapter<Event, EventsViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder.create(parent, imageLoader, onItemClick)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(
                oldItem: Event,
                newItem: Event
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Event,
                newItem: Event
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}