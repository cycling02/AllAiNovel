package com.cycling.core.ui.theme

import androidx.compose.ui.graphics.Color

object IOSColors {
    val Blue = Color(0xFF007AFF)
    val Green = Color(0xFF34C759)
    val Indigo = Color(0xFF5856D6)
    val Orange = Color(0xFFFF9500)
    val Pink = Color(0xFFFF2D55)
    val Purple = Color(0xFFAF52DE)
    val Red = Color(0xFFFF3B30)
    val Teal = Color(0xFF5AC8FA)
    val Yellow = Color(0xFFFFCC00)
}

data class GrayColors(
    val Gray: Color,
    val Gray2: Color,
    val Gray3: Color,
    val Gray4: Color,
    val Gray5: Color,
    val Gray6: Color
)

val IOSLightGrayColors = GrayColors(
    Gray = Color(0xFF8E8E93),
    Gray2 = Color(0xFFAEAEB2),
    Gray3 = Color(0xFFC7C7CC),
    Gray4 = Color(0xFFD1D1D6),
    Gray5 = Color(0xFFE5E5EA),
    Gray6 = Color(0xFFF2F2F7)
)

val IOSDarkGrayColors = GrayColors(
    Gray = Color(0xFF8E8E93),
    Gray2 = Color(0xFF636366),
    Gray3 = Color(0xFF48484A),
    Gray4 = Color(0xFF3A3A3C),
    Gray5 = Color(0xFF2C2C2E),
    Gray6 = Color(0xFF1C1C1E)
)

object IOSLightColors {
    val Background = Color(0xFFFFFFFF)
    val SecondaryBackground = Color(0xFFF2F2F7)
    val TertiaryBackground = Color(0xFFFFFFFF)
    val GroupedBackground = Color(0xFFF2F2F7)
    val SecondaryGroupedBackground = Color(0xFFFFFFFF)
    val TertiaryGroupedBackground = Color(0xFFF2F2F7)
    val Separator = Color(0xFFC6C6C8)
    val OpaqueSeparator = Color(0xFFC6C6C8)
    val Label = Color(0xFF000000)
    val SecondaryLabel = Color(0x99000000)
    val TertiaryLabel = Color(0x4D000000)
    val QuaternaryLabel = Color(0x2D000000)
}

object IOSDarkColors {
    val Background = Color(0xFF000000)
    val SecondaryBackground = Color(0xFF1C1C1E)
    val TertiaryBackground = Color(0xFF2C2C2E)
    val GroupedBackground = Color(0xFF000000)
    val SecondaryGroupedBackground = Color(0xFF1C1C1E)
    val TertiaryGroupedBackground = Color(0xFF2C2C2E)
    val Separator = Color(0xFF38383A)
    val OpaqueSeparator = Color(0xFF38383A)
    val Label = Color(0xFFFFFFFF)
    val SecondaryLabel = Color(0x99FFFFFF)
    val TertiaryLabel = Color(0x4DFFFFFF)
    val QuaternaryLabel = Color(0x2DFFFFFF)
}

val PaperBackground = Color(0xFFFFF8E7)
val PaperBackgroundEyeCare = Color(0xFFE8F5E9)
val PaperBackgroundNight = Color(0xFF1E1A14)
