package com.cycling.feature.chapter



sealed interface ChapterListEffect {
    data class NavigateToChapterEdit(val chapterId: Long) : ChapterListEffect
    data class ShowError(val message: String) : ChapterListEffect
    data object ChapterAdded : ChapterListEffect
    data object ChapterDeleted : ChapterListEffect
}
