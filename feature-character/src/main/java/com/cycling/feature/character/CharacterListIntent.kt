package com.cycling.feature.character

import com.cycling.domain.model.Character

sealed interface CharacterListIntent {
    data object LoadCharacters : CharacterListIntent
    data object ShowAddDialog : CharacterListIntent
    data object HideAddDialog : CharacterListIntent
    data class ShowEditDialog(val character: Character) : CharacterListIntent
    data object HideEditDialog : CharacterListIntent
    data class ShowDeleteDialog(val character: Character) : CharacterListIntent
    data object HideDeleteDialog : CharacterListIntent
    data object ConfirmDelete : CharacterListIntent
    data class DeleteCharacter(val character: Character) : CharacterListIntent
    data class UndoDelete(val character: Character) : CharacterListIntent
    data class AddCharacter(val character: Character) : CharacterListIntent
    data class UpdateCharacter(val character: Character) : CharacterListIntent
    data class SearchCharacters(val query: String) : CharacterListIntent
    data object ShowAiGenerateDialog : CharacterListIntent
    data object HideAiGenerateDialog : CharacterListIntent
    data class GenerateCharacter(
        val characterType: String?,
        val gender: String?,
        val description: String?,
        val count: Int
    ) : CharacterListIntent
    data object ApplyAiCharacters : CharacterListIntent
    data object HideAiPreviewDialog : CharacterListIntent
}
