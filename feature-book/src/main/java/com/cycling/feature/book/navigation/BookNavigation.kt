package com.cycling.feature.book.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.cycling.feature.book.ui.BookListScreen

fun NavController.navigateToBookList(navOptions: NavOptions? = null) {
    navigate(BookList, navOptions)
}

fun NavGraphBuilder.bookListScreen(
    onNavigateToChapters: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToTools: () -> Unit
) {
    composable<BookList> {
        BookListScreen(
            onNavigateToChapters = onNavigateToChapters,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToStatistics = onNavigateToStatistics,
            onNavigateToTools = onNavigateToTools
        )
    }
}
