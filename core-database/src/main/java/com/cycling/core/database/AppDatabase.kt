package com.cycling.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cycling.core.database.dao.ApiConfigDao
import com.cycling.core.database.dao.BookDao
import com.cycling.core.database.dao.CharacterDao
import com.cycling.core.database.dao.CharacterRelationDao
import com.cycling.core.database.dao.ChapterDao
import com.cycling.core.database.dao.InspirationDao
import com.cycling.core.database.dao.NameFavoriteDao
import com.cycling.core.database.dao.OutlineDao
import com.cycling.core.database.dao.PromptDao
import com.cycling.core.database.dao.WritingSessionDao
import com.cycling.core.database.dao.WorldSettingDao
import com.cycling.core.database.entity.ApiConfigEntity
import com.cycling.core.database.entity.BookEntity
import com.cycling.core.database.entity.CharacterEntity
import com.cycling.core.database.entity.CharacterRelationEntity
import com.cycling.core.database.entity.ChapterEntity
import com.cycling.core.database.entity.InspirationEntity
import com.cycling.core.database.entity.NameFavoriteEntity
import com.cycling.core.database.entity.OutlineItemEntity
import com.cycling.core.database.entity.PromptEntity
import com.cycling.core.database.entity.WritingSessionEntity
import com.cycling.core.database.entity.WorldSettingEntity

@Database(
    entities = [
        BookEntity::class,
        ChapterEntity::class,
        ApiConfigEntity::class,
        OutlineItemEntity::class,
        CharacterEntity::class,
        CharacterRelationEntity::class,
        WorldSettingEntity::class,
        NameFavoriteEntity::class,
        InspirationEntity::class,
        PromptEntity::class,
        WritingSessionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun apiConfigDao(): ApiConfigDao
    abstract fun outlineDao(): OutlineDao
    abstract fun characterDao(): CharacterDao
    abstract fun characterRelationDao(): CharacterRelationDao
    abstract fun worldSettingDao(): WorldSettingDao
    abstract fun nameFavoriteDao(): NameFavoriteDao
    abstract fun inspirationDao(): InspirationDao
    abstract fun promptDao(): PromptDao
    abstract fun writingSessionDao(): WritingSessionDao
}
