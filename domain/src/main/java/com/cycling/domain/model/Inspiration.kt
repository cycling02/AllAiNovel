package com.cycling.domain.model

data class Inspiration(
    val id: Long = 0,
    val title: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
