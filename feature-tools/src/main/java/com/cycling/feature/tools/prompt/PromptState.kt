package com.cycling.feature.tools.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory

data class PromptState(
    val isLoading: Boolean = false,
    val prompts: List<Prompt> = emptyList(),
    val filteredPrompts: List<Prompt> = emptyList(),
    val selectedCategory: PromptCategory? = null,
    val showFavoritesOnly: Boolean = false,
    val searchQuery: String = "",
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingPrompt: Prompt? = null,
    val showDeleteConfirm: Boolean = false,
    val promptToDelete: Prompt? = null,
    val error: String? = null,
    val message: String? = null
) {
    val displayPrompts: List<Prompt>
        get() = if (searchQuery.isBlank() && selectedCategory == null && !showFavoritesOnly) {
            prompts
        } else {
            filteredPrompts
        }

    val isEmpty: Boolean
        get() = displayPrompts.isEmpty() && !isLoading
}
