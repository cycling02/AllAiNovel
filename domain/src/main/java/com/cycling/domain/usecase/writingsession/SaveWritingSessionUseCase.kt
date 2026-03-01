package com.cycling.domain.usecase.writingsession

import com.cycling.domain.model.WritingSession
import com.cycling.domain.repository.WritingSessionRepository
import javax.inject.Inject

class SaveWritingSessionUseCase @Inject constructor(
    private val repository: WritingSessionRepository
) {
    suspend operator fun invoke(session: WritingSession): Long {
        return repository.saveSession(session)
    }
}
