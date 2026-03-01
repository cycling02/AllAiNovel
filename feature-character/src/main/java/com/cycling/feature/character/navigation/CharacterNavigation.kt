package com.cycling.feature.character.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cycling.feature.character.ui.CharacterListScreen

fun NavController.navigateToCharacterList(bookId: Long) {
    navigate(CharacterList(bookId))
}

fun NavGraphBuilder.characterListScreen(
    onNavigateBack: () -> Unit
) {
    composable<CharacterList> { backStackEntry ->
        val route = backStackEntry.toRoute<CharacterList>()
        CharacterListScreen(
            bookId = route.bookId,
            onNavigateBack = onNavigateBack
        )
    }
}
