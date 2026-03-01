package com.cycling.feature.editor.model

import com.cycling.domain.model.ApiConfig

data class ChapterEditState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isAiLoading: Boolean = false,
    val content: String = "",
    val title: String = "",
    val apiConfig: ApiConfig? = null,
    val aiResult: String? = null,
    val showAiResult: Boolean = false,
    val error: String? = null,
    val hasUnsavedChanges: Boolean = false,
    val initialWordCount: Int = 0,
    val sessionStartTime: Long = 0L
)
