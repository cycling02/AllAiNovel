package com.cycling.feature.book.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.cycling.feature.book.ui.BookListScreen

const val BOOK_LIST_ROUTE = "book_list"

fun NavController.navigateToBookList(navOptions: NavOptions? = null) {
    navigate(BOOK_LIST_ROUTE, navOptions)
}

fun NavGraphBuilder.bookListScreen(
    onNavigateToChapters: (Long) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    composable(route = BOOK_LIST_ROUTE) {
        BookListScreen(
            onNavigateToChapters = onNavigateToChapters,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}