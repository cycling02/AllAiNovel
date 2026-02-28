package com.cycling.domain.repository

import com.cycling.domain.model.ApiConfig

interface AiRepository {
    suspend fun continueWriting(
        config: ApiConfig,
        context: String,
        maxTokens: Int = 1000
    ): Result<String>
}
