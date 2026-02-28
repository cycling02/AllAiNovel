package com.cycling.core.database.di

import android.content.Context
import androidx.room.Room
import com.cycling.core.database.AppDatabase
import com.cycling.core.database.dao.ApiConfigDao
import com.cycling.core.database.dao.BookDao
import com.cycling.core.database.dao.ChapterDao
import com.cycling.core.database.repository.ApiConfigRepositoryImpl
import com.cycling.core.database.repository.BookRepositoryImpl
import com.cycling.core.database.repository.ChapterRepositoryImpl
import com.cycling.domain.repository.ApiConfigRepository
import com.cycling.domain.repository.BookRepository
import com.cycling.domain.repository.ChapterRepository
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
        ).build()
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
}
