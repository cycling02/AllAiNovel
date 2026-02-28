package com.cycling.feature.editor.model

sealed interface ChapterEditEffect {
    data class ShowSnackbar(val message: String) : ChapterEditEffect
    data object NavigateBack : ChapterEditEffect
    data class ShowToast(val message: String) : ChapterEditEffect
}
