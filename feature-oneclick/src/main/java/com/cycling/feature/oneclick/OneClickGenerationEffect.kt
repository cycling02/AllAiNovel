package com.cycling.feature.oneclick

sealed class OneClickGenerationEffect {
    object NavigateBack : OneClickGenerationEffect()
    data class NavigateToEditor(val chapterId: Long) : OneClickGenerationEffect()
    data class ShowToast(val message: String) : OneClickGenerationEffect()
    data class ShowError(val message: String) : OneClickGenerationEffect()
}
