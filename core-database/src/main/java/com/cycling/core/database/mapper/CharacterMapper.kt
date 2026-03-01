package com.cycling.core.database.mapper

import com.cycling.core.database.entity.CharacterEntity
import com.cycling.core.database.entity.CharacterRelationEntity
import com.cycling.domain.model.Character
import com.cycling.domain.model.CharacterRelation
import com.cycling.domain.model.RelationType

fun CharacterEntity.toModel(): Character {
    return Character(
        id = id,
        bookId = bookId,
        name = name,
        alias = alias,
        gender = gender,
        age = age,
        personality = personality,
        appearance = appearance,
        background = background,
        notes = notes,
        avatarPath = avatarPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        bookId = bookId,
        name = name,
        alias = alias,
        gender = gender,
        age = age,
        personality = personality,
        appearance = appearance,
        background = background,
        notes = notes,
        avatarPath = avatarPath,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CharacterRelationEntity.toModel(): CharacterRelation {
    return CharacterRelation(
        id = id,
        characterId = characterId,
        relatedCharacterId = relatedCharacterId,
        relationType = runCatching { RelationType.valueOf(relationType) }.getOrDefault(RelationType.OTHER),
        description = description,
        createdAt = createdAt
    )
}

fun CharacterRelation.toEntity(): CharacterRelationEntity {
    return CharacterRelationEntity(
        id = id,
        characterId = characterId,
        relatedCharacterId = relatedCharacterId,
        relationType = relationType.name,
        description = description,
        createdAt = createdAt
    )
}
