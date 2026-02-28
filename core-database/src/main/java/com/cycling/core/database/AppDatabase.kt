package com.cycling.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cycling.core.database.dao.ApiConfigDao
import com.cycling.core.database.dao.BookDao
import com.cycling.core.database.dao.ChapterDao
import com.cycling.core.database.entity.ApiConfigEntity
import com.cycling.core.database.entity.BookEntity
import com.cycling.core.database.entity.ChapterEntity

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        ApiConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun apiConfigDao(): ApiConfigDao
}
