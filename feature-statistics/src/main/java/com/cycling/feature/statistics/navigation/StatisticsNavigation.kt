package com.cycling.feature.statistics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.cycling.feature.statistics.ui.StatisticsScreen

fun NavGraphBuilder.statisticsScreen(
    onNavigateBack: () -> Unit
) {
    composable<Statistics> {
        StatisticsScreen(
            onNavigateBack = onNavigateBack
        )
    }
}

fun NavController.navigateToStatistics() {
    navigate(Statistics)
}
