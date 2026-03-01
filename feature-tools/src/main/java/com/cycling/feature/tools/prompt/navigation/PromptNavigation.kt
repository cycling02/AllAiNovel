package com.cycling.feature.tools.prompt.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.tools.prompt.ui.PromptListScreen

fun NavController.navigateToPromptManagement() {
    navigate(PromptManagement)
}

fun NavGraphBuilder.promptManagementScreen(
    onNavigateBack: () -> Unit
) {
    composable<PromptManagement> {
        PromptListScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
