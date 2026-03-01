package com.cycling.domain.model

enum class ChapterStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}

data class Chapter(
    val id: Long = 0,
    val bookId: Long,
    val outlineItemId: Long? = null,
    val title: String,
    val content: String = "",
    val chapterNumber: Int = 0,
    val wordCount: Int = 0,
    val status: ChapterStatus = ChapterStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
