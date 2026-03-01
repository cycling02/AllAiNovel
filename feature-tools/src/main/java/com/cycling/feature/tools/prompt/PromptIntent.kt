package com.cycling.feature.tools.prompt

import com.cycling.domain.model.Prompt
import com.cycling.domain.model.PromptCategory

sealed interface PromptIntent {
    data object LoadPrompts : PromptIntent
    data class SelectCategory(val category: PromptCategory?) : PromptIntent
    data object ToggleFavoritesFilter : PromptIntent
    data class SearchPrompts(val query: String) : PromptIntent
    data object ShowAddDialog : PromptIntent
    data object HideAddDialog : PromptIntent
    data class ShowEditDialog(val prompt: Prompt) : PromptIntent
    data object HideEditDialog : PromptIntent
    data class ShowDeleteConfirm(val prompt: Prompt) : PromptIntent
    data object HideDeleteConfirm : PromptIntent
    data class CreatePrompt(val prompt: Prompt) : PromptIntent
    data class UpdatePrompt(val prompt: Prompt) : PromptIntent
    data class DeletePrompt(val prompt: Prompt) : PromptIntent
    data class ToggleFavorite(val prompt: Prompt) : PromptIntent
    data object ClearMessage : PromptIntent
    data object ClearError : PromptIntent
}
