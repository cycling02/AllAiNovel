package com.cycling.allainovel.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cycling.feature.ai.navigation.AiWriting
import com.cycling.feature.ai.navigation.aiWritingScreen
import com.cycling.feature.book.navigation.BookList
import com.cycling.feature.book.navigation.bookListScreen
import com.cycling.feature.book.navigation.navigateToBookList
import com.cycling.feature.character.navigation.characterListScreen
import com.cycling.feature.character.navigation.navigateToCharacterList
import com.cycling.feature.worldbuilding.navigation.navigateToWorldSettingList
import com.cycling.feature.worldbuilding.navigation.worldSettingListScreen
import com.cycling.feature.chapter.navigation.chapterListScreen
import com.cycling.feature.chapter.navigation.navigateToChapterList
import com.cycling.feature.editor.navigation.chapterEditScreen
import com.cycling.feature.editor.navigation.navigateToChapterEdit
import com.cycling.feature.outline.navigation.navigateToOutlineList
import com.cycling.feature.outline.navigation.outlineListScreen
import com.cycling.feature.settings.navigation.Settings
import com.cycling.feature.settings.navigation.settingsScreen
import com.cycling.feature.statistics.navigation.Statistics
import com.cycling.feature.statistics.navigation.statisticsScreen
import com.cycling.feature.tools.navigation.Tools
import com.cycling.feature.tools.navigation.toolsScreen
import com.cycling.feature.tools.inspiration.navigation.inspirationListScreen
import com.cycling.feature.tools.inspiration.navigation.navigateToInspirationList
import com.cycling.feature.tools.namegenerator.navigation.nameGeneratorScreen
import com.cycling.feature.tools.namegenerator.navigation.navigateToNameGenerator
import com.cycling.feature.tools.prompt.navigation.navigateToPromptManagement
import com.cycling.feature.tools.prompt.navigation.promptManagementScreen
import com.cycling.feature.oneclick.navigation.oneClickGenerationScreen
import com.cycling.feature.oneclick.navigation.navigateToOneClickGeneration

// 底部导航栏的路由列表
val bottomNavRoutes = listOf(
    BottomNavRoute.Books,
    BottomNavRoute.AiWriting,
    BottomNavRoute.Tools,
    BottomNavRoute.Statistics,
    BottomNavRoute.Settings
)

sealed class BottomNavRoute(
    val route: Any,
    val label: String
) {
    data object Books : BottomNavRoute(BookList, "书籍")
    data object AiWriting : BottomNavRoute(com.cycling.feature.ai.navigation.AiWriting(), "AI写作")
    data object Tools : BottomNavRoute(com.cycling.feature.tools.navigation.Tools, "工具")
    data object Statistics : BottomNavRoute(com.cycling.feature.statistics.navigation.Statistics, "统计")
    data object Settings : BottomNavRoute(com.cycling.feature.settings.navigation.Settings, "设置")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // 使用 currentBackStackEntryAsState 确保状态更新
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // 检查当前是否在底部导航栏的某个页面上
    val isBottomNavDestination = bottomNavRoutes.any { route ->
        currentDestination?.hierarchy?.any { 
            it.route?.startsWith(route.route::class.qualifiedName ?: "") == true 
        } == true
    }
    
    // 定义进入动画
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(300)) +
        slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { it / 4 }
        )
    }
    
    // 定义退出动画
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { -it / 4 }
        )
    }
    
    // 定义返回进入动画
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(animationSpec = tween(300)) +
        slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { -it / 4 }
        )
    }
    
    // 定义返回退出动画
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(animationSpec = tween(300)) +
        slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { it / 4 }
        )
    }
    
    Scaffold(
        bottomBar = {
            // 只在底部导航栏页面显示
            if (isBottomNavDestination) {
                BottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // 弹出到起始目的地，保存状态
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // 避免重复创建相同页面
                            launchSingleTop = true
                            // 恢复之前保存的状态
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BookList,
            modifier = Modifier.padding(paddingValues),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition
        ) {
            bookListScreen(
                onNavigateToChapters = { bookId ->
                    navController.navigateToChapterList(bookId)
                },
                onNavigateToSettings = {
                    navController.navigate(Settings)
                },
                onNavigateToStatistics = {
                    navController.navigate(Statistics)
                },
                onNavigateToTools = {
                    navController.navigate(Tools)
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
}
