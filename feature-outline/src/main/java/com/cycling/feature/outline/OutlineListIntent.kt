package com.cycling.feature.outline

import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus

sealed interface OutlineListIntent {
    data object LoadOutline : OutlineListIntent
    data class ShowAddDialog(val parent: OutlineItem? = null) : OutlineListIntent
    data object HideAddDialog : OutlineListIntent
    data class ShowEditDialog(val item: OutlineItem) : OutlineListIntent
    data object HideEditDialog : OutlineListIntent
    data class ShowDeleteDialog(val item: OutlineItem) : OutlineListIntent
    data object HideDeleteDialog : OutlineListIntent
    data class AddOutlineItem(
        val title: String,
        val summary: String,
        val parent: OutlineItem?
    ) : OutlineListIntent
    data class UpdateOutlineItem(
        val item: OutlineItem,
        val title: String,
        val summary: String,
        val status: OutlineStatus
    ) : OutlineListIntent
    data class DeleteOutlineItem(val deleteChildren: Boolean) : OutlineListIntent
    data class ToggleExpand(val itemId: Long) : OutlineListIntent
    data class ReorderItems(val items: List<OutlineItem>) : OutlineListIntent
    data object ShowAiGenerateDialog : OutlineListIntent
    data object HideAiGenerateDialog : OutlineListIntent
    data class GenerateOutline(
        val topic: String,
        val summary: String,
        val chapterCount: Int,
        val levelCount: Int
    ) : OutlineListIntent
    data object ApplyAiOutline : OutlineListIntent
    data object HideAiPreviewDialog : OutlineListIntent
    data class GenerateChapterFromOutline(val item: OutlineItem) : OutlineListIntent
    data class NavigateToChapter(val chapterId: Long) : OutlineListIntent
}
