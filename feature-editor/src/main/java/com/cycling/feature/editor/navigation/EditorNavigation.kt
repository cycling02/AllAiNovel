package com.cycling.feature.editor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.editor.ui.ChapterEditScreen

fun NavGraphBuilder.chapterEditScreen(
    onNavigateBack: () -> Unit
) {
    composable<ChapterEdit> { backStackEntry ->
        val route = backStackEntry.toRoute<ChapterEdit>()
        ChapterEditScreen(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToChapterEdit(chapterId: Long) {
    navigate(ChapterEdit(chapterId))
}
