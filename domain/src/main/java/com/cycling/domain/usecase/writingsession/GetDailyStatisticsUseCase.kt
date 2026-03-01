package com.cycling.domain.usecase.writingsession

import com.cycling.domain.model.DailyStatistics
import com.cycling.domain.repository.WritingSessionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetDailyStatisticsUseCase @Inject constructor(
    private val repository: WritingSessionRepository
) {
    operator fun invoke(days: Int = 7): Flow<List<DailyStatistics>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1)
        val startTime = calendar.timeInMillis
        
        val endTime = System.currentTimeMillis()
        
        return repository.getDailyStatistics(startTime, endTime)
    }
    
    operator fun invoke(startTime: Long, endTime: Long): Flow<List<DailyStatistics>> {
        return repository.getDailyStatistics(startTime, endTime)
    }
}
