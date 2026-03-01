package com.cycling.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cycling.core.database.entity.WritingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WritingSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: WritingSessionEntity): Long

    @Query("SELECT * FROM writing_sessions WHERE startTime >= :startTime AND startTime < :endTime ORDER BY startTime DESC")
    fun getSessionsByDateRange(startTime: Long, endTime: Long): Flow<List<WritingSessionEntity>>

    @Query("SELECT * FROM writing_sessions WHERE bookId = :bookId ORDER BY startTime DESC")
    fun getSessionsByBookId(bookId: Long): Flow<List<WritingSessionEntity>>

    @Query("SELECT * FROM writing_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WritingSessionEntity>>

    @Query("SELECT SUM(endWordCount - startWordCount) FROM writing_sessions WHERE startTime >= :startTime AND startTime < :endTime")
    fun getWordsWrittenInDateRange(startTime: Long, endTime: Long): Flow<Int?>

    @Query("SELECT SUM(duration) FROM writing_sessions WHERE startTime >= :startTime AND startTime < :endTime")
    fun getDurationInDateRange(startTime: Long, endTime: Long): Flow<Long?>

    @Query("SELECT COUNT(*) FROM writing_sessions WHERE startTime >= :startTime AND startTime < :endTime")
    fun getSessionCountInDateRange(startTime: Long, endTime: Long): Flow<Int>

    @Query("DELETE FROM writing_sessions WHERE bookId = :bookId")
    suspend fun deleteByBookId(bookId: Long)
}
