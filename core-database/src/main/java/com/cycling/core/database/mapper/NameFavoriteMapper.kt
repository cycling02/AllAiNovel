package com.cycling.core.database.mapper

import com.cycling.core.database.entity.NameFavoriteEntity
import com.cycling.domain.model.NameFavorite
import com.cycling.domain.model.NameType

fun NameFavoriteEntity.toModel(): NameFavorite {
    return NameFavorite(
        id = id,
        name = name,
        type = runCatching { NameType.valueOf(type) }.getOrDefault(NameType.PERSON_NAME),
        createdAt = createdAt
    )
}

fun NameFavorite.toEntity(): NameFavoriteEntity {
    return NameFavoriteEntity(
        id = id,
        name = name,
        type = type.name,
        createdAt = createdAt
    )
}
