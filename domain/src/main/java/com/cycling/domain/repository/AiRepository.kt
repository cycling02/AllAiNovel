package com.cycling.domain.repository

import com.cycling.domain.model.ApiConfig
import kotlinx.coroutines.flow.Flow

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

    suspend fun generateContent(
        config: ApiConfig,
        prompt: String,
        maxTokens: Int = 2000
    ): Result<String>

    fun continueWritingStream(
        config: ApiConfig,
        context: String,
        maxTokens: Int = 1000
    ): Flow<String>

    fun continueWritingWithContextStream(
        config: ApiConfig,
        context: String,
        bookContext: String,
        maxTokens: Int = 1000
    ): Flow<String>

    suspend fun parseUserDescription(
        config: ApiConfig,
        description: String
    ): Result<String>

    suspend fun generateBookInfo(
        config: ApiConfig,
        parsedData: String
    ): Result<String>

    suspend fun generateCharactersBatch(
        config: ApiConfig,
        characterHints: List<String>
    ): Result<String>

    suspend fun generateWorldSettingsBatch(
        config: ApiConfig,
        worldHints: List<String>
    ): Result<String>

    suspend fun generateOutlineStructure(
        config: ApiConfig,
        bookInfo: String,
        outlineHint: String
    ): Result<String>

    suspend fun generateChapterContent(
        config: ApiConfig,
        bookInfo: String,
        characters: String,
        worldSettings: String,
        outline: String,
        chapterHint: String
    ): Result<String>
}
