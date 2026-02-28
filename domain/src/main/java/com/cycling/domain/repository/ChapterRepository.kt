package com.cycling.domain.repository

import com.cycling.domain.model.Chapter
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    fun getChaptersByBookId(bookId: Long): Flow<List<Chapter>>
    fun getChapterById(id: Long): Flow<Chapter?>
    suspend fun insertChapter(chapter: Chapter): Long
    suspend fun updateChapter(chapter: Chapter)
    suspend fun deleteChapter(chapter: Chapter)
    suspend fun deleteChapterById(id: Long)
    suspend fun deleteChaptersByBookId(bookId: Long)
    suspend fun getNextChapterNumber(bookId: Long): Int
}
