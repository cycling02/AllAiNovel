package com.cycling.feature.ai

sealed class AiWritingEffect {
    data class ShowToast(val message: String) : AiWritingEffect()
    data object NavigateBack : AiWritingEffect()
    data class ApplyContentToEditor(val content: String) : AiWritingEffect()
}
