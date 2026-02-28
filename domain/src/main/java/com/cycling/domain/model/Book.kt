package com.cycling.domain.model

enum class BookStatus {
    DRAFT,
    ONGOING,
    COMPLETED,
    PAUSED
}

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String = "",
    val description: String = "",
    val coverPath: String? = null,
    val status: BookStatus = BookStatus.DRAFT,
    val wordCount: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
