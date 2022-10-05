package com.severo.challenge.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import com.severo.challenge.databinding.ItemEventsBinding
import com.severo.challenge.framework.common.GenericViewHolder
import com.severo.challenge.framework.imageloader.ImageLoader
import com.severo.challenge.util.OnEventItemClick

class FavoritesViewHolder(
    itemBinding: ItemEventsBinding,
    private val imageLoader: ImageLoader,
    private val onItemClick: OnEventItemClick<FavoriteItem>
) : GenericViewHolder<FavoriteItem>(itemBinding) {

    private val textTitle = itemBinding.textTitle
    private val textDescription = itemBinding.textDescription
    private val imageEvent = itemBinding.imageEvent

    override fun bind(data: FavoriteItem) {
        textTitle.text = data.title
        textTitle.transitionName = data.title

        textDescription.text = data.description
        textDescription.transitionName = data.description

        imageEvent.transitionName = data.image
        imageLoader.load(imageEvent, data.image)

        itemView.setOnClickListener {
            onItemClick.invoke(data, imageEvent, textTitle, textDescription)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader,
            onItemClick: OnEventItemClick<FavoriteItem>
        ): FavoritesViewHolder {
            val itemBinding = ItemEventsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return FavoritesViewHolder(itemBinding, imageLoader, onItemClick)
        }
    }
}