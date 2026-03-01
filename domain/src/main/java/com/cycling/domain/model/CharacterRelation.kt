package com.cycling.domain.model

data class CharacterRelation(
    val id: Long = 0,
    val characterId: Long,
    val relatedCharacterId: Long,
    val relationType: RelationType,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
