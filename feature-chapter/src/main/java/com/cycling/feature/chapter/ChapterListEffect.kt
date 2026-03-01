package com.cycling.feature.chapter

import com.cycling.domain.model.Chapter

sealed interface ChapterListEffect {
    data class NavigateToChapterEdit(val chapterId: Long) : ChapterListEffect
    data class ShowError(val message: String) : ChapterListEffect
    data object ChapterAdded : ChapterListEffect
    data object ChapterRestored : ChapterListEffect
    data class ShowUndoSnackbar(val message: String, val chapter: Chapter) : ChapterListEffect
}
