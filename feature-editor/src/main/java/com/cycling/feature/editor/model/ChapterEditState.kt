package com.cycling.feature.editor.model

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.usecase.context.BookContext

data class ChapterEditState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isAiLoading: Boolean = false,
    val content: String = "",
    val title: String = "",
    val apiConfig: ApiConfig? = null,
    val error: String? = null,
    val hasUnsavedChanges: Boolean = false,
    val initialWordCount: Int = 0,
    val sessionStartTime: Long = 0L,
    val isStreaming: Boolean = false,
    val bookContext: BookContext? = null,
    val useContext: Boolean = true
)
