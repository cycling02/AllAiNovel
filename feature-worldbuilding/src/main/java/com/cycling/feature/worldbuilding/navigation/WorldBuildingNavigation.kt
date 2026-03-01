package com.cycling.feature.worldbuilding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.worldbuilding.ui.WorldSettingListScreen

/**
 * 导航到世界观设定列表页面。
 *
 * @param bookId 书籍ID，用于加载该书籍下的所有世界观设定
 */
fun NavController.navigateToWorldSettingList(bookId: Long) {
    navigate(WorldSettingList(bookId))
}

/**
 * 注册世界观设定列表页面的导航图。
 *
 * @param onNavigateBack 返回回调
 */
fun NavGraphBuilder.worldSettingListScreen(
    onNavigateBack: () -> Unit
) {
    composable<WorldSettingList> { backStackEntry ->
        val route = backStackEntry.toRoute<WorldSettingList>()
        WorldSettingListScreen(
            bookId = route.bookId,
            onNavigateBack = onNavigateBack
        )
    }
}
