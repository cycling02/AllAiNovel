package com.cycling.feature.tools.prompt

sealed interface PromptEffect {
    data class ShowToast(val message: String) : PromptEffect
    data class ShowError(val error: String) : PromptEffect
}
