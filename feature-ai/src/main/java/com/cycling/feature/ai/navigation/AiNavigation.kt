package com.cycling.feature.ai.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cycling.feature.ai.ui.AiWritingScreen

const val AI_WRITING_ROUTE = "ai_writing?context={context}"

object AiDestinations {
    const val AI_WRITING = "ai_writing"
    const val CONTEXT_ARG = "context"

    fun aiWritingRoute(context: String = ""): String {
        return if (context.isNotEmpty()) {
            "ai_writing?context=${java.net.URLEncoder.encode(context, "UTF-8")}"
        } else {
            "ai_writing"
        }
    }
}

fun NavGraphBuilder.aiFeatureGraph(
    navController: NavController,
    onApplyContent: (String) -> Unit
) {
    composable(
        route = AI_WRITING_ROUTE,
        arguments = listOf(
            navArgument(AiDestinations.CONTEXT_ARG) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val context = backStackEntry.arguments?.getString(AiDestinations.CONTEXT_ARG) ?: ""
        val decodedContext = if (context.isNotEmpty()) {
            java.net.URLDecoder.decode(context, "UTF-8")
        } else {
            ""
        }

        AiWritingScreen(
            initialContext = decodedContext,
            onNavigateBack = { navController.popBackStack() },
            onApplyContent = onApplyContent
        )
    }
}

fun NavController.navigateToAiWriting(context: String = "") {
    navigate(AiDestinations.aiWritingRoute(context))
}
