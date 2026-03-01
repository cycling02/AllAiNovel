package com.cycling.feature.tools.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.tools.ui.ToolsScreen

fun NavController.navigateToTools() {
    navigate(Tools)
}

fun NavGraphBuilder.toolsScreen(
    onNavigateToInspiration: () -> Unit,
    onNavigateToNameGenerator: () -> Unit,
    onNavigateToPromptManagement: () -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Tools> {
        ToolsScreen(
            onNavigateToInspiration = onNavigateToInspiration,
            onNavigateToNameGenerator = onNavigateToNameGenerator,
            onNavigateToPromptManagement = onNavigateToPromptManagement,
            onNavigateBack = onNavigateBack
        )
    }
}
