package com.cycling.feature.character

import com.cycling.domain.model.Character

sealed interface CharacterListEffect {
    data class ShowToast(val message: String) : CharacterListEffect
    data class ShowError(val message: String) : CharacterListEffect
    data class ShowUndoSnackbar(val message: String, val character: Character) : CharacterListEffect
    data class NavigateToDetail(val characterId: Long) : CharacterListEffect
    data object NavigateBack : CharacterListEffect
}
