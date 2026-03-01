package com.cycling.feature.outline

sealed interface OutlineListEffect {
    data class ShowToast(val message: String) : OutlineListEffect
    data class ShowError(val message: String) : OutlineListEffect
    data object NavigateBack : OutlineListEffect
    data class NavigateToChapter(val chapterId: Long) : OutlineListEffect
}
