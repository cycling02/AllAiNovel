package com.cycling.allainovel.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object BookList : Screen("book_list")
    
    object ChapterList : Screen("book_list/{bookId}") {
        fun createRoute(bookId: Long) = "book_list/$bookId"
        const val bookIdArg = "bookId"
    }
    
    object ChapterEdit : Screen("chapter_edit/{chapterId}") {
        fun createRoute(chapterId: Long) = "chapter_edit/$chapterId"
        const val chapterIdArg = "chapterId"
    }
    
    object Settings : Screen("settings")
}