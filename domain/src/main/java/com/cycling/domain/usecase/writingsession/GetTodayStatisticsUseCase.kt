package com.cycling.domain.usecase.writingsession

import com.cycling.domain.repository.WritingSessionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetTodayStatisticsUseCase @Inject constructor(
    private val repository: WritingSessionRepository
) {
    data class TodayStats(
        val wordsWritten: Int,
        val duration: Long,
        val sessionCount: Int
    )
    
    operator fun invoke(): Flow<TodayStats> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()
        
        return kotlinx.coroutines.flow.combine(
            repository.getWordsWrittenInDateRange(startTime, endTime),
            repository.getDurationInDateRange(startTime, endTime),
            repository.getSessionCountInDateRange(startTime, endTime)
        ) { words, duration, count ->
            TodayStats(
                wordsWritten = words,
                duration = duration,
                sessionCount = count
            )
        }
    }
}
