package com.cycling.domain.usecase.writingsession

import com.cycling.domain.model.WritingSession
import com.cycling.domain.repository.WritingSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWritingSessionsByDateRangeUseCase @Inject constructor(
    private val repository: WritingSessionRepository
) {
    operator fun invoke(startTime: Long, endTime: Long): Flow<List<WritingSession>> {
        return repository.getSessionsByDateRange(startTime, endTime)
    }
}
