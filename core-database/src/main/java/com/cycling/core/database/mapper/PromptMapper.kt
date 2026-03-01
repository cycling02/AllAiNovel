package com.cycling.core.database.mapper

import com.cycling.core.database.entity.PromptEntity
import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory

fun PromptEntity.toModel(): Prompt {
    return Prompt(
        id = id,
        title = title,
        content = content,
        category = try {
            PromptCategory.valueOf(category)
        } catch (e: IllegalArgumentException) {
            PromptCategory.CUSTOM
        },
        isSystem = isSystem,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Prompt.toEntity(): PromptEntity {
    return PromptEntity(
        id = id,
        title = title,
        content = content,
        category = category.name,
        isSystem = isSystem,
        isFavorite = isFavorite,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
