package com.cycling.feature.editor.model

sealed interface ChapterEditIntent {
    data class UpdateContent(val content: String) : ChapterEditIntent
    data object SaveChapter : ChapterEditIntent
    data object ContinueWriting : ChapterEditIntent
    data object StopStreaming : ChapterEditIntent
    data object ClearError : ChapterEditIntent
    data object ToggleUseContext : ChapterEditIntent
}
