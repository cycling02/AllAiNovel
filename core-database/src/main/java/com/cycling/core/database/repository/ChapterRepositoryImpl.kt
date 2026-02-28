package com.cycling.core.database.repository

import com.cycling.core.database.dao.ChapterDao
import com.cycling.core.database.mapper.toModel
import com.cycling.core.database.mapper.toEntity
import com.cycling.domain.model.Chapter
import com.cycling.domain.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepositoryImpl @Inject constructor(
    private val chapterDao: ChapterDao
) : ChapterRepository {
    override fun getChaptersByBookId(bookId: Long): Flow<List<Chapter>> {
        return chapterDao.getChaptersByBookId(bookId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getChapterById(id: Long): Flow<Chapter?> {
        return chapterDao.getChapterById(id).map { entity ->
            entity?.toModel()
        }
    }

    override suspend fun insertChapter(chapter: Chapter): Long {
        return chapterDao.insert(chapter.toEntity())
    }

    override suspend fun updateChapter(chapter: Chapter) {
        chapterDao.update(chapter.toEntity())
    }

    override suspend fun deleteChapter(chapter: Chapter) {
        chapterDao.delete(chapter.toEntity())
    }

    override suspend fun deleteChapterById(id: Long) {
        chapterDao.deleteById(id)
    }

    override suspend fun deleteChaptersByBookId(bookId: Long) {
        chapterDao.deleteByBookId(bookId)
    }

    override suspend fun getNextChapterNumber(bookId: Long): Int {
        return chapterDao.getNextChapterNumber(bookId)
    }
}