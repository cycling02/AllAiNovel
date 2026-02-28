package com.cycling.feature.settings.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.settings.ui.SettingsScreen

const val SETTINGS_ROUTE = "settings"

fun NavController.navigateToSettings() {
    navigate(SETTINGS_ROUTE)
}

fun NavGraphBuilder.settingsScreen(
    onNavigateBack: () -> Unit
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
