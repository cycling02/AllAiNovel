package com.cycling.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class EditorTheme {
    Light, EyeCare, Night
}

val IOSLightColorScheme = lightColorScheme(
    primary = IOSColors.Blue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001D3C),
    secondary = IOSLightGrayColors.Gray,
    onSecondary = Color.White,
    secondaryContainer = IOSLightGrayColors.Gray5,
    onSecondaryContainer = Color(0xFF1A1A1A),
    tertiary = IOSColors.Teal,
    onTertiary = Color.White,
    background = IOSLightColors.GroupedBackground,
    onBackground = IOSLightColors.Label,
    surface = IOSLightColors.SecondaryGroupedBackground,
    onSurface = IOSLightColors.Label,
    surfaceVariant = IOSLightGrayColors.Gray5,
    onSurfaceVariant = IOSLightColors.SecondaryLabel,
    error = IOSColors.Red,
    onError = Color.White,
    errorContainer = Color(0xFFFFE5E5),
    onErrorContainer = Color(0xFF4D0000),
    outline = IOSLightGrayColors.Gray3,
    outlineVariant = IOSLightGrayColors.Gray4,
    inverseSurface = Color(0xFF1A1A1A),
    inverseOnSurface = IOSLightGrayColors.Gray5,
    inversePrimary = Color(0xFF9ECBFF)
)

val IOSDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF004E8C),
    onPrimaryContainer = Color(0xFFD0E4FF),
    secondary = IOSDarkGrayColors.Gray,
    onSecondary = Color.White,
    secondaryContainer = IOSDarkGrayColors.Gray5,
    onSecondaryContainer = IOSLightGrayColors.Gray5,
    tertiary = Color(0xFF64D2FF),
    onTertiary = Color.White,
    background = IOSDarkColors.GroupedBackground,
    onBackground = IOSDarkColors.Label,
    surface = IOSDarkColors.SecondaryGroupedBackground,
    onSurface = IOSDarkColors.Label,
    surfaceVariant = IOSDarkGrayColors.Gray5,
    onSurfaceVariant = IOSDarkColors.SecondaryLabel,
    error = Color(0xFFFF453A),
    onError = Color.White,
    errorContainer = Color(0xFF690000),
    onErrorContainer = Color(0xFFFFE5E5),
    outline = IOSDarkGrayColors.Gray3,
    outlineVariant = IOSDarkGrayColors.Gray4,
    inverseSurface = IOSLightGrayColors.Gray5,
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = IOSColors.Blue
)

data class PaperColors(
    val background: Color,
    val surface: Color,
    val onBackground: Color,
    val onSurface: Color,
    val primary: Color
)

val LocalPaperColors = staticCompositionLocalOf {
    PaperColors(
        background = Color(0xFFFFF8E7),
        surface = Color(0xFFFBF6EE),
        onBackground = Color(0xFF2C2416),
        onSurface = Color(0xFF2C2416),
        primary = Color(0xFFC84B31)
    )
}

val LocalEditorTheme = staticCompositionLocalOf { EditorTheme.Light }

val LocalIOSColors = staticCompositionLocalOf { IOSColors }

val LocalGrayColors = staticCompositionLocalOf { IOSLightGrayColors }

fun getPaperColors(editorTheme: EditorTheme): PaperColors = when (editorTheme) {
    EditorTheme.Light -> PaperColors(
        background = Color(0xFFFFF8E7),
        surface = Color(0xFFFBF6EE),
        onBackground = Color(0xFF2C2416),
        onSurface = Color(0xFF2C2416),
        primary = Color(0xFFC84B31)
    )
    EditorTheme.EyeCare -> PaperColors(
        background = Color(0xFFE8F5E9),
        surface = Color(0xFFF1F8E9),
        onBackground = Color(0xFF37474F),
        onSurface = Color(0xFF37474F),
        primary = Color(0xFF2E7D32)
    )
    EditorTheme.Night -> PaperColors(
        background = Color(0xFF1E1A14),
        surface = Color(0xFF2D2A24),
        onBackground = Color(0xFFE8DCC8),
        onSurface = Color(0xFFE8DCC8),
        primary = Color(0xFFD4A574)
    )
}

@Composable
fun AllAiNovelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    editorTheme: EditorTheme = EditorTheme.Light,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) IOSDarkColorScheme else IOSLightColorScheme
    val paperColors = getPaperColors(editorTheme)
    val grayColors = if (darkTheme) IOSDarkGrayColors else IOSLightGrayColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalPaperColors provides paperColors,
        LocalEditorTheme provides editorTheme,
        LocalIOSColors provides IOSColors,
        LocalGrayColors provides grayColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
