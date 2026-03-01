package com.cycling.domain.model

data class NameFavorite(
    val id: Long = 0,
    val name: String,
    val type: NameType,
    val createdAt: Long = System.currentTimeMillis()
)
