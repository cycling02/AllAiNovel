package com.cycling.feature.editor.navigation

import kotlinx.serialization.Serializable

object EditorRoutes {
    
    @Serializable
    data class ChapterEdit(val chapterId: Long) {
        companion object {
            const val CHAPTER_ID_ARG = "chapterId"
        }
    }
}
