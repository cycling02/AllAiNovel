package com.cycling.core.database.di

import android.content.Context
import androidx.room.Room
import com.cycling.core.database.AppDatabase
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
import com.cycling.core.database.repository.ApiConfigRepositoryImpl
import com.cycling.core.database.repository.BookRepositoryImpl
import com.cycling.core.database.repository.CharacterRepositoryImpl
import com.cycling.core.database.repository.ChapterRepositoryImpl
import com.cycling.core.database.repository.InspirationRepositoryImpl
import com.cycling.core.database.repository.NameFavoriteRepositoryImpl
import com.cycling.core.database.repository.OutlineRepositoryImpl
import com.cycling.core.database.repository.PromptRepositoryImpl
import com.cycling.core.database.repository.WritingSessionRepositoryImpl
import com.cycling.core.database.repository.WorldSettingRepositoryImpl
import com.cycling.domain.repository.ApiConfigRepository
import com.cycling.domain.repository.BookRepository
import com.cycling.domain.repository.CharacterRepository
import com.cycling.domain.repository.ChapterRepository
import com.cycling.domain.repository.InspirationRepository
import com.cycling.domain.repository.NameFavoriteRepository
import com.cycling.domain.repository.OutlineRepository
import com.cycling.domain.repository.PromptRepository
import com.cycling.domain.repository.WritingSessionRepository
import com.cycling.domain.repository.WorldSettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ainovel_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    @Singleton
    fun provideChapterDao(database: AppDatabase): ChapterDao {
        return database.chapterDao()
    }

    @Provides
    @Singleton
    fun provideApiConfigDao(database: AppDatabase): ApiConfigDao {
        return database.apiConfigDao()
    }

    @Provides
    @Singleton
    fun provideOutlineDao(database: AppDatabase): OutlineDao {
        return database.outlineDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: AppDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideCharacterRelationDao(database: AppDatabase): CharacterRelationDao {
        return database.characterRelationDao()
    }

    @Provides
    @Singleton
    fun provideWorldSettingDao(database: AppDatabase): WorldSettingDao {
        return database.worldSettingDao()
    }

    @Provides
    @Singleton
    fun provideWritingSessionDao(database: AppDatabase): WritingSessionDao {
        return database.writingSessionDao()
    }

    @Provides
    @Singleton
    fun provideBookRepository(bookDao: BookDao): BookRepository {
        return BookRepositoryImpl(bookDao)
    }

    @Provides
    @Singleton
    fun provideChapterRepository(chapterDao: ChapterDao): ChapterRepository {
        return ChapterRepositoryImpl(chapterDao)
    }

    @Provides
    @Singleton
    fun provideApiConfigRepository(apiConfigDao: ApiConfigDao): ApiConfigRepository {
        return ApiConfigRepositoryImpl(apiConfigDao)
    }

    @Provides
    @Singleton
    fun provideOutlineRepository(outlineDao: OutlineDao): OutlineRepository {
        return OutlineRepositoryImpl(outlineDao)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        characterDao: CharacterDao,
        characterRelationDao: CharacterRelationDao
    ): CharacterRepository {
        return CharacterRepositoryImpl(characterDao, characterRelationDao)
    }

    @Provides
    @Singleton
    fun provideWorldSettingRepository(
        worldSettingDao: WorldSettingDao
    ): WorldSettingRepository {
        return WorldSettingRepositoryImpl(worldSettingDao)
    }

    @Provides
    @Singleton
    fun provideWritingSessionRepository(
        writingSessionDao: WritingSessionDao,
        bookDao: BookDao,
        chapterDao: ChapterDao
    ): WritingSessionRepository {
        return WritingSessionRepositoryImpl(writingSessionDao, bookDao, chapterDao)
    }

    @Provides
    @Singleton
    fun providePromptDao(database: AppDatabase): PromptDao {
        return database.promptDao()
    }

    @Provides
    @Singleton
    fun providePromptRepository(promptDao: PromptDao): PromptRepository {
        return PromptRepositoryImpl(promptDao)
    }

    @Provides
    @Singleton
    fun provideInspirationDao(database: AppDatabase): InspirationDao {
        return database.inspirationDao()
    }

    @Provides
    @Singleton
    fun provideNameFavoriteDao(database: AppDatabase): NameFavoriteDao {
        return database.nameFavoriteDao()
    }

    @Provides
    @Singleton
    fun provideInspirationRepository(inspirationDao: InspirationDao): InspirationRepository {
        return InspirationRepositoryImpl(inspirationDao)
    }

    @Provides
    @Singleton
    fun provideNameFavoriteRepository(nameFavoriteDao: NameFavoriteDao): NameFavoriteRepository {
        return NameFavoriteRepositoryImpl(nameFavoriteDao)
    }
}
