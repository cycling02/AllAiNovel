package com.cycling.feature.chapter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cycling.feature.chapter.ui.ChapterListScreen

object ChapterListRoutes {
    const val ChapterList = "chapter_list/{bookId}"
    fun chapterList(bookId: Long) = "chapter_list/$bookId"
}

@Composable
fun ChapterListNavigation(
    navController: NavHostController,
    onNavigateBack: () -> Unit,
    onNavigateToChapterEdit: (Long) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ChapterListRoutes.ChapterList
    ) {
        composable(
            route = ChapterListRoutes.ChapterList,
            arguments = listOf(
                navArgument("bookId") { type = NavType.LongType }
            )
        ) {
            ChapterListScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToChapterEdit = onNavigateToChapterEdit
            )
        }
    }
}
