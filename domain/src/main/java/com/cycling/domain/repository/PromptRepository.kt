package com.cycling.domain.repository

import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory
import kotlinx.coroutines.flow.Flow

interface PromptRepository {
    suspend fun createPrompt(prompt: Prompt): Long
    suspend fun updatePrompt(prompt: Prompt)
    suspend fun deletePrompt(id: Long)
    suspend fun getPromptByIdSync(id: Long): Prompt?
    fun getPromptById(id: Long): Flow<Prompt?>
    fun getAllPrompts(): Flow<List<Prompt>>
    fun getPromptsByCategory(category: PromptCategory): Flow<List<Prompt>>
    fun getFavoritePrompts(): Flow<List<Prompt>>
    fun searchPrompts(query: String): Flow<List<Prompt>>
    suspend fun toggleFavorite(id: Long)
    suspend fun initializeSystemPrompts()
}
