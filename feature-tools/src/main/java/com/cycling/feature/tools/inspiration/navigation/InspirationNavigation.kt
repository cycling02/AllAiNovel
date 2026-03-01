package com.cycling.feature.tools.inspiration.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.tools.inspiration.ui.InspirationListScreen

fun NavController.navigateToInspirationList() {
    navigate(InspirationList)
}

fun NavGraphBuilder.inspirationListScreen(
    onNavigateBack: () -> Unit
) {
    composable<InspirationList> {
        InspirationListScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
