package com.severo.challenge.framework.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.severo.core.data.DbConstants
import com.severo.core.domain.model.Event

@Entity(tableName = DbConstants.FAVORITES_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_TITLE)
    val title: String,
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_PRICE)
    val price: Double,
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_IMAGE)
    val image: String,
    @ColumnInfo(name = DbConstants.FAVORITES_COLUMN_INFO_DESCRIPTION)
    val description: String
)

fun List<FavoriteEntity>.toEventsModel() = map {
    Event(
        description = it.description,
        image = it.image,
        price = it.price,
        title = it.title,
        id = it.id
    )
}
