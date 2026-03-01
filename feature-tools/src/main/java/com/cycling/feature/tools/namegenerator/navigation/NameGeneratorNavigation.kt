package com.cycling.feature.tools.namegenerator.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.tools.namegenerator.ui.NameGeneratorScreen

fun NavController.navigateToNameGenerator() {
    navigate(NameGenerator)
}

fun NavGraphBuilder.nameGeneratorScreen(
    onNavigateBack: () -> Unit
) {
    composable<NameGenerator> {
        NameGeneratorScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
