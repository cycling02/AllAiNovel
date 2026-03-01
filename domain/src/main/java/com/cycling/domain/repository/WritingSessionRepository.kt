package com.cycling.domain.repository

import com.cycling.domain.model.DailyStatistics
import com.cycling.domain.model.TotalStatistics
import com.cycling.domain.model.WritingSession
import kotlinx.coroutines.flow.Flow

interface WritingSessionRepository {
    suspend fun saveSession(session: WritingSession): Long
    fun getSessionsByDateRange(startTime: Long, endTime: Long): Flow<List<WritingSession>>
    fun getSessionsByBookId(bookId: Long): Flow<List<WritingSession>>
    fun getAllSessions(): Flow<List<WritingSession>>
    fun getWordsWrittenInDateRange(startTime: Long, endTime: Long): Flow<Int>
    fun getDurationInDateRange(startTime: Long, endTime: Long): Flow<Long>
    fun getSessionCountInDateRange(startTime: Long, endTime: Long): Flow<Int>
    fun getDailyStatistics(startTime: Long, endTime: Long): Flow<List<DailyStatistics>>
    fun getTotalStatistics(): Flow<TotalStatistics>
    suspend fun deleteSessionsByBookId(bookId: Long)
}
