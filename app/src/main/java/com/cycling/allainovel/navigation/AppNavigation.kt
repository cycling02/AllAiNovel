package com.cycling.allainovel.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.cycling.feature.ai.navigation.AiWriting
import com.cycling.feature.ai.navigation.aiWritingScreen
import com.cycling.feature.ai.navigation.navigateToAiWriting
import com.cycling.feature.book.navigation.BookList
import com.cycling.feature.book.navigation.bookListScreen
import com.cycling.feature.book.navigation.navigateToBookList
import com.cycling.feature.character.navigation.characterListScreen
import com.cycling.feature.character.navigation.navigateToCharacterList
import com.cycling.feature.worldbuilding.navigation.navigateToWorldSettingList
import com.cycling.feature.worldbuilding.navigation.worldSettingListScreen
import com.cycling.feature.chapter.navigation.ChapterList
import com.cycling.feature.chapter.navigation.chapterListScreen
import com.cycling.feature.chapter.navigation.navigateToChapterList
import com.cycling.feature.editor.navigation.chapterEditScreen
import com.cycling.feature.editor.navigation.navigateToChapterEdit
import com.cycling.feature.outline.navigation.navigateToOutlineList
import com.cycling.feature.outline.navigation.outlineListScreen
import com.cycling.feature.settings.navigation.Settings
import com.cycling.feature.settings.navigation.navigateToSettings
import com.cycling.feature.settings.navigation.settingsScreen
import com.cycling.feature.statistics.navigation.navigateToStatistics
import com.cycling.feature.statistics.navigation.statisticsScreen
import com.cycling.feature.tools.navigation.navigateToTools
import com.cycling.feature.tools.navigation.toolsScreen
import com.cycling.feature.tools.inspiration.navigation.inspirationListScreen
import com.cycling.feature.tools.inspiration.navigation.navigateToInspirationList
import com.cycling.feature.tools.namegenerator.navigation.nameGeneratorScreen
import com.cycling.feature.tools.namegenerator.navigation.navigateToNameGenerator
import com.cycling.feature.tools.prompt.navigation.navigateToPromptManagement
import com.cycling.feature.tools.prompt.navigation.promptManagementScreen
import com.cycling.feature.oneclick.navigation.OneClickGeneration
import com.cycling.feature.oneclick.navigation.oneClickGenerationScreen
import com.cycling.feature.oneclick.navigation.navigateToOneClickGeneration

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BookList
    ) {
        bookListScreen(
            onNavigateToChapters = { bookId ->
                navController.navigateToChapterList(bookId)
            },
            onNavigateToSettings = {
                navController.navigateToSettings()
            },
            onNavigateToStatistics = {
                navController.navigateToStatistics()
            },
            onNavigateToTools = {
                navController.navigateToTools()
            },
            onNavigateToOneClickGeneration = {
                navController.navigateToOneClickGeneration()
            }
        )

        chapterListScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToChapterEdit = { chapterId ->
                navController.navigateToChapterEdit(chapterId, isEditable = false)
            },
            onNavigateToOutline = { bookId ->
                navController.navigateToOutlineList(bookId)
            },
            onNavigateToCharacter = { bookId ->
                navController.navigateToCharacterList(bookId)
            },
            onNavigateToWorldBuilding = { bookId ->
                navController.navigateToWorldSettingList(bookId)
            }
        )

        chapterEditScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        settingsScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        aiWritingScreen(
            onNavigateBack = { navController.popBackStack() },
            onApplyContent = { }
        )

        outlineListScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToChapter = { chapterId ->
                navController.navigateToChapterEdit(chapterId)
            }
        )

        characterListScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        worldSettingListScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        statisticsScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        toolsScreen(
            onNavigateToInspiration = {
                navController.navigateToInspirationList()
            },
            onNavigateToNameGenerator = {
                navController.navigateToNameGenerator()
            },
            onNavigateToPromptManagement = {
                navController.navigateToPromptManagement()
            },
            onNavigateBack = { navController.popBackStack() }
        )

        inspirationListScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        nameGeneratorScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        promptManagementScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        oneClickGenerationScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEditor = { chapterId ->
                navController.navigateToChapterEdit(chapterId)
            }
        )
    }
}
