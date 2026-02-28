package com.cycling.feature.chapter

import com.cycling.domain.model.Chapter

sealed interface ChapterListIntent {
    data object LoadChapters : ChapterListIntent
    data object ShowAddDialog : ChapterListIntent
    data object HideAddDialog : ChapterListIntent
    data class AddChapter(val title: String) : ChapterListIntent
    data class ShowDeleteDialog(val chapter: Chapter) : ChapterListIntent
    data object HideDeleteDialog : ChapterListIntent
    data object ConfirmDelete : ChapterListIntent
}
