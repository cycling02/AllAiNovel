package com.cycling.feature.outline

import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus

data class OutlineListState(
    val outlineItems: List<OutlineItem> = emptyList(),
    val expandedIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val itemToEdit: OutlineItem? = null,
    val itemToDelete: OutlineItem? = null,
    val parentForNewItem: OutlineItem? = null,
    val error: String? = null,
    val showAiGenerateDialog: Boolean = false,
    val isAiGenerating: Boolean = false,
    val aiGeneratedOutline: List<OutlineItem>? = null,
    val showAiPreviewDialog: Boolean = false
) {
    val isEmpty: Boolean
        get() = outlineItems.isEmpty() && !isLoading
}

data class OutlineItemUiModel(
    val item: OutlineItem,
    val level: Int,
    val isExpanded: Boolean,
    val hasChildren: Boolean
)
