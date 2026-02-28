package com.cycling.feature.ai

data class AiWritingState(
    val isLoading: Boolean = false,
    val context: String = "",
    val selectedMode: AiWritingMode = AiWritingMode.CONTINUE,
    val generatedResult: String = "",
    val lastPrompt: String = "",
    val error: String? = null
)
