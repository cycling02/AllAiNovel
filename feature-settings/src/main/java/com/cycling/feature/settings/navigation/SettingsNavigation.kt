package com.cycling.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.settings.ui.SettingsScreen

fun NavController.navigateToSettings() {
    navigate(Settings)
}

fun NavGraphBuilder.settingsScreen(
    onNavigateBack: () -> Unit
) {
    composable<Settings> {
        SettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
