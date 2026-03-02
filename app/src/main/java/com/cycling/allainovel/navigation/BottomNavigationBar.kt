package com.cycling.allainovel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    ) {
        bottomNavRoutes.forEach { route ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route?.startsWith(route.route::class.qualifiedName ?: "") == true
            } == true
            
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = when (route) {
                            is BottomNavRoute.Books -> Icons.Filled.Book
                            is BottomNavRoute.AiWriting -> Icons.Filled.AutoAwesome
                            is BottomNavRoute.Tools -> Icons.Filled.Build
                            is BottomNavRoute.Statistics -> Icons.Filled.TrendingUp
                            is BottomNavRoute.Settings -> Icons.Filled.Settings
                        },
                        contentDescription = route.label
                    )
                },
                label = { Text(route.label) },
                selected = isSelected,
                onClick = { onNavigate(route.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
