package com.cycling.core.database.repository

import com.cycling.core.database.dao.BookDao
import com.cycling.core.database.dao.ChapterDao
import com.cycling.core.database.dao.WritingSessionDao
import com.cycling.core.database.mapper.toModel
import com.cycling.core.database.mapper.toEntity
import com.cycling.domain.model.DailyStatistics
import com.cycling.domain.model.TotalStatistics
import com.cycling.domain.model.WritingSession
import com.cycling.domain.repository.WritingSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WritingSessionRepositoryImpl @Inject constructor(
    private val writingSessionDao: WritingSessionDao,
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao
) : WritingSessionRepository {

    override suspend fun saveSession(session: WritingSession): Long {
        return writingSessionDao.insert(session.toEntity())
    }

    override fun getSessionsByDateRange(startTime: Long, endTime: Long): Flow<List<WritingSession>> {
        return writingSessionDao.getSessionsByDateRange(startTime, endTime).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getSessionsByBookId(bookId: Long): Flow<List<WritingSession>> {
        return writingSessionDao.getSessionsByBookId(bookId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getAllSessions(): Flow<List<WritingSession>> {
        return writingSessionDao.getAllSessions().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getWordsWrittenInDateRange(startTime: Long, endTime: Long): Flow<Int> {
        return writingSessionDao.getWordsWrittenInDateRange(startTime, endTime).map { it ?: 0 }
    }

    override fun getDurationInDateRange(startTime: Long, endTime: Long): Flow<Long> {
        return writingSessionDao.getDurationInDateRange(startTime, endTime).map { it ?: 0L }
    }

    override fun getSessionCountInDateRange(startTime: Long, endTime: Long): Flow<Int> {
        return writingSessionDao.getSessionCountInDateRange(startTime, endTime)
    }

    override fun getDailyStatistics(startTime: Long, endTime: Long): Flow<List<DailyStatistics>> {
        return getSessionsByDateRange(startTime, endTime).map { sessions ->
            val calendar = Calendar.getInstance()
            sessions.groupBy { session ->
                calendar.timeInMillis = session.startTime
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }.map { (date, daySessions) ->
                DailyStatistics(
                    date = date,
                    totalWords = daySessions.sumOf { it.wordsWritten },
                    totalDuration = daySessions.sumOf { it.duration },
                    sessionCount = daySessions.size
                )
            }.sortedByDescending { it.date }
        }
    }

    override fun getTotalStatistics(): Flow<TotalStatistics> {
        return combine(
            writingSessionDao.getAllSessions(),
            bookDao.getAllBooks(),
            chapterDao.getAllChapters()
        ) { sessions, books, chapters ->
            TotalStatistics(
                totalWords = sessions.sumOf { (it.endWordCount - it.startWordCount).toLong() },
                totalDuration = sessions.sumOf { it.duration },
                totalSessions = sessions.size,
                totalChapters = chapters.size,
                totalBooks = books.size
            )
        }
    }

    override suspend fun deleteSessionsByBookId(bookId: Long) {
        writingSessionDao.deleteByBookId(bookId)
    }
}
