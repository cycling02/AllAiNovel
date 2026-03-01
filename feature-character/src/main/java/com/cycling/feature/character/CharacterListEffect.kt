package com.cycling.feature.character

sealed interface CharacterListEffect {
    data class ShowToast(val message: String) : CharacterListEffect
    data class ShowError(val message: String) : CharacterListEffect
    data class NavigateToDetail(val characterId: Long) : CharacterListEffect
    data object NavigateBack : CharacterListEffect
}
