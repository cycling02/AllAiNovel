package com.cycling.core.database.repository

import com.cycling.core.database.dao.PromptDao
import com.cycling.core.database.mapper.toEntity
import com.cycling.core.database.mapper.toModel
import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory
import com.cycling.domain.repository.PromptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromptRepositoryImpl @Inject constructor(
    private val promptDao: PromptDao
) : PromptRepository {
    override suspend fun createPrompt(prompt: Prompt): Long {
        return promptDao.insert(prompt.toEntity())
    }

    override suspend fun updatePrompt(prompt: Prompt) {
        promptDao.update(prompt.toEntity())
    }

    override suspend fun deletePrompt(id: Long) {
        promptDao.deleteById(id)
    }

    override suspend fun getPromptByIdSync(id: Long): Prompt? {
        return promptDao.getByIdSync(id)?.toModel()
    }

    override fun getPromptById(id: Long): Flow<Prompt?> {
        return promptDao.getById(id).map { entity ->
            entity?.toModel()
        }
    }

    override fun getAllPrompts(): Flow<List<Prompt>> {
        return promptDao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getPromptsByCategory(category: PromptCategory): Flow<List<Prompt>> {
        return promptDao.getByCategory(category.name).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getFavoritePrompts(): Flow<List<Prompt>> {
        return promptDao.getFavorites().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun searchPrompts(query: String): Flow<List<Prompt>> {
        return promptDao.search(query).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun toggleFavorite(id: Long) {
        promptDao.toggleFavorite(id)
    }

    override suspend fun initializeSystemPrompts() {
        if (promptDao.getSystemPromptsCount() > 0) return

        val systemPrompts = listOf(
            Prompt(
                title = "智能续写",
                content = "请根据以下内容，自然地续写后面的情节发展，保持文风一致，字数在300-500字左右：\n\n{{context}}",
                category = PromptCategory.CONTINUE_WRITING,
                isSystem = true
            ),
            Prompt(
                title = "改写优化",
                content = "请改写以下内容，使其更加生动有趣，保持原意不变：\n\n{{context}}",
                category = PromptCategory.REWRITE,
                isSystem = true
            ),
            Prompt(
                title = "扩写细节",
                content = "请将以下内容进行扩写，增加更多细节描写，使内容更加丰富：\n\n{{context}}",
                category = PromptCategory.EXPAND,
                isSystem = true
            ),
            Prompt(
                title = "润色精修",
                content = "请润色以下内容，优化语言表达，使其更加流畅优美：\n\n{{context}}",
                category = PromptCategory.POLISH,
                isSystem = true
            )
        )

        systemPrompts.forEach { prompt ->
            promptDao.insert(prompt.toEntity())
        }
    }
}
