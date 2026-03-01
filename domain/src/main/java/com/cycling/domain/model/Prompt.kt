package com.cycling.domain.model

data class Prompt(
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: PromptCategory,
    val isSystem: Boolean = false,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
