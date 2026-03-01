package com.cycling.core.database.mapper

import com.cycling.core.database.entity.InspirationEntity
import com.cycling.domain.model.Inspiration

fun InspirationEntity.toModel(): Inspiration {
    return Inspiration(
        id = id,
        title = title,
        content = content,
        tags = if (tags.isNotEmpty()) tags.split(",") else emptyList(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Inspiration.toEntity(): InspirationEntity {
    return InspirationEntity(
        id = id,
        title = title,
        content = content,
        tags = tags.joinToString(","),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
