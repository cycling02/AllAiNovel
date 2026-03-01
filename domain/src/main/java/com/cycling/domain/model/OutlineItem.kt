package com.cycling.domain.model

data class OutlineItem(
    val id: Long = 0,
    val bookId: Long,
    val parentId: Long? = null,
    val chapterId: Long? = null,
    val title: String,
    val summary: String = "",
    val level: Int = 0,
    val sortOrder: Int = 0,
    val status: OutlineStatus = OutlineStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
