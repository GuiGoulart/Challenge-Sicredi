package com.severo.challenge.framework.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.severo.challenge.framework.data.local.db.dao.FavoriteDao
import com.severo.challenge.framework.data.local.db.entity.FavoriteEntity

@Database(
    entities = [
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
}