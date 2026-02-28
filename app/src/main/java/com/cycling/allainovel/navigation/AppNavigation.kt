package com.cycling.allainovel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cycling.feature.book.ui.BookListScreen
import com.cycling.feature.editor.ui.ChapterEditScreen
import com.cycling.feature.chapter.ui.ChapterListScreen
import com.cycling.feature.settings.ui.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.BookList.route
    ) {
        composable(route = Screen.BookList.route) {
            BookListScreen(
                onNavigateToChapters = { bookId ->
                    navController.navigate(Screen.ChapterList.createRoute(bookId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(
            route = Screen.ChapterList.route,
            arguments = listOf(
                navArgument(Screen.ChapterList.bookIdArg) {
                    type = NavType.LongType
                }
            )
        ) {
            ChapterListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChapterEdit = { chapterId ->
                    navController.navigate(Screen.ChapterEdit.createRoute(chapterId))
                }
            )
        }
        
        composable(
            route = Screen.ChapterEdit.route,
            arguments = listOf(
                navArgument(Screen.ChapterEdit.chapterIdArg) {
                    type = NavType.LongType
                }
            )
        ) {
            ChapterEditScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}