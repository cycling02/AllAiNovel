package com.cycling.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cycling.core.ui.theme.LocalPaperColors

@Composable
fun PaperBackground(
    modifier: Modifier = Modifier,
    showPageShadow: Boolean = true,
    pageShadowWidth: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val paperColors = LocalPaperColors.current
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(paperColors.background)
            .then(
                if (showPageShadow) {
                    Modifier.drawBehind {
                        val shadowWidth = pageShadowWidth.toPx()
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.15f),
                                    Color.Transparent
                                ),
                                startX = 0f,
                                endX = shadowWidth
                            ),
                            topLeft = Offset(0f, 0f),
                            size = size.copy(width = shadowWidth)
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {
        content()
    }
}

@Composable
fun PaperContent(
    modifier: Modifier = Modifier,
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val paperColors = LocalPaperColors.current
    
    Box(
        modifier = modifier
            .background(paperColors.surface)
            .padding(contentPadding)
    ) {
        content()
    }
}
