package com.cycling.feature.outline.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.outline.ui.OutlineListScreen

fun NavController.navigateToOutlineList(bookId: Long) {
    navigate(OutlineList(bookId))
}

fun NavGraphBuilder.outlineListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChapter: (Long) -> Unit
) {
    composable<OutlineList> { backStackEntry ->
        val route = backStackEntry.toRoute<OutlineList>()
        OutlineListScreen(
            bookId = route.bookId,
            onNavigateBack = onNavigateBack,
            onNavigateToChapter = onNavigateToChapter
        )
    }
}
