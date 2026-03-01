package com.cycling.feature.ai.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.ai.ui.AiWritingScreen

fun NavController.navigateToAiWriting(context: String = "") {
    navigate(AiWriting(context))
}

fun NavGraphBuilder.aiWritingScreen(
    onNavigateBack: () -> Unit,
    onApplyContent: (String) -> Unit
) {
    composable<AiWriting> { backStackEntry ->
        val route = backStackEntry.toRoute<AiWriting>()
        AiWritingScreen(
            initialContext = route.context,
            onNavigateBack = onNavigateBack,
            onApplyContent = onApplyContent
        )
    }
}
