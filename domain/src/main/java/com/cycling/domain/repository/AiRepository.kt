package com.cycling.domain.repository

import com.cycling.domain.model.ApiConfig

interface AiRepository {
    suspend fun continueWriting(
        config: ApiConfig,
        context: String,
        maxTokens: Int = 1000
    ): Result<String>

    suspend fun generateOutline(
        config: ApiConfig,
        topic: String,
        summary: String,
        chapterCount: Int,
        levelCount: Int
    ): Result<String>

    suspend fun generateCharacter(
        config: ApiConfig,
        characterType: String?,
        gender: String?,
        description: String?,
        count: Int
    ): Result<String>
}
