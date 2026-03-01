package com.cycling.feature.oneclick.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.cycling.feature.oneclick.ui.OneClickGenerationScreen
import kotlinx.serialization.Serializable

@Serializable
object OneClickGeneration

fun NavController.navigateToOneClickGeneration(navOptions: NavOptions? = null) {
    navigate(OneClickGeneration, navOptions)
}

fun NavGraphBuilder.oneClickGenerationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditor: (Long) -> Unit
) {
    composable<OneClickGeneration> {
        OneClickGenerationScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToEditor = onNavigateToEditor
        )
    }
}
