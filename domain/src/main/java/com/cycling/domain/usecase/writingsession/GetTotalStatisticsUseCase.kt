package com.cycling.domain.usecase.writingsession

import com.cycling.domain.model.TotalStatistics
import com.cycling.domain.repository.WritingSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalStatisticsUseCase @Inject constructor(
    private val repository: WritingSessionRepository
) {
    operator fun invoke(): Flow<TotalStatistics> {
        return repository.getTotalStatistics()
    }
}
