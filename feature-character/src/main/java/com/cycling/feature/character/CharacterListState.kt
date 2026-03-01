package com.cycling.feature.character

import com.cycling.domain.model.Character

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val filteredCharacters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val characterToEdit: Character? = null,
    val characterToDelete: Character? = null,
    val searchQuery: String = "",
    val error: String? = null,
    val showAiGenerateDialog: Boolean = false,
    val isAiGenerating: Boolean = false,
    val aiGeneratedCharacters: List<Character>? = null,
    val showAiPreviewDialog: Boolean = false
) {
    val displayCharacters: List<Character>
        get() = if (searchQuery.isBlank()) characters else filteredCharacters
    
    val isEmpty: Boolean
        get() = displayCharacters.isEmpty() && !isLoading
}
