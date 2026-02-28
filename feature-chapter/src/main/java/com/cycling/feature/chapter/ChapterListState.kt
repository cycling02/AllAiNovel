package com.cycling.feature.chapter

import com.cycling.domain.model.Chapter

data class ChapterListState(
    val chapters: List<Chapter> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val chapterToDelete: Chapter? = null,
    val bookId: Long = 0,
    val error: String? = null
)
