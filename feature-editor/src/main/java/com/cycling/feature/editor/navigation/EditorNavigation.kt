package com.cycling.feature.editor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.editor.ui.ChapterEditScreen

fun NavGraphBuilder.editorNavigationGraph(
    navController: NavHostController
) {
    composable<EditorRoutes.ChapterEdit> { backStackEntry ->
        val route = backStackEntry.toRoute<EditorRoutes.ChapterEdit>()
        ChapterEditScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

fun NavHostController.navigateToChapterEdit(chapterId: Long) {
    navigate(EditorRoutes.ChapterEdit(chapterId))
}
