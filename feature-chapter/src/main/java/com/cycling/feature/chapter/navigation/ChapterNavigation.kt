package com.cycling.feature.chapter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.chapter.ui.ChapterListScreen

fun NavController.navigateToChapterList(bookId: Long) {
    navigate(ChapterList(bookId))
}

fun NavGraphBuilder.chapterListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChapterEdit: (Long) -> Unit,
    onNavigateToOutline: (Long) -> Unit,
    onNavigateToCharacter: (Long) -> Unit,
    onNavigateToWorldBuilding: (Long) -> Unit
) {
    composable<ChapterList> { backStackEntry ->
        val route = backStackEntry.toRoute<ChapterList>()
        ChapterListScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToChapterEdit = onNavigateToChapterEdit,
            onNavigateToOutline = onNavigateToOutline,
            onNavigateToCharacter = onNavigateToCharacter,
            onNavigateToWorldBuilding = onNavigateToWorldBuilding
        )
    }
}
