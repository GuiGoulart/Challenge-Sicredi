package com.severo.challenge.presentation.events.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.severo.challenge.databinding.ItemEventsBinding
import com.severo.challenge.framework.imageloader.ImageLoader
import com.severo.challenge.util.OnEventItemClick
import com.severo.core.domain.model.Event

class EventsViewHolder(
    itemEventsBinding: ItemEventsBinding,
    private val imageLoader: ImageLoader,
    private val onItemClick: OnEventItemClick<Event>
) : RecyclerView.ViewHolder(itemEventsBinding.root) {

    private val textTitle = itemEventsBinding.textTitle
    private val textDescription = itemEventsBinding.textDescription
    private val imageEvent = itemEventsBinding.imageEvent

    fun bind(event: Event) {
        textTitle.text = event.title
        textTitle.transitionName = event.title

        textDescription.text = event.description
        textDescription.transitionName = event.description

        imageEvent.transitionName = event.image
        imageLoader.load(imageEvent, event.image)

        itemView.setOnClickListener {
            onItemClick.invoke(event, imageEvent, textTitle, textDescription)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader,
            onItemClick: OnEventItemClick<Event>
        ): EventsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemEventsBinding.inflate(inflater, parent, false)
            return EventsViewHolder(itemBinding, imageLoader, onItemClick)
        }
    }
}