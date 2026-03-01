package com.cycling.feature.ai

import com.cycling.domain.model.Prompt

data class AiWritingState(
    val isLoading: Boolean = false,
    val context: String = "",
    val selectedMode: AiWritingMode = AiWritingMode.CONTINUE,
    val selectedPrompt: Prompt? = null,
    val prompts: List<Prompt> = emptyList(),
    val generatedResult: String = "",
    val lastPrompt: String = "",
    val error: String? = null
)
