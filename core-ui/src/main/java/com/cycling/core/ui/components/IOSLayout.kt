package com.cycling.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IOSScreen(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        content = content
    )
}

@Composable
fun IOSPageContent(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = IOSSpacing.lg,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding),
        content = content
    )
}

@Composable
fun IOSSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            IOSSectionHeader(title = title)
        }
        
        IOSCard(padding = 0.dp) {
            Column(content = content)
        }
        
        if (footer != null) {
            IOSSectionFooter(text = footer)
        }
    }
}

@Composable
fun IOSSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = IOSSpacing.xl,
                vertical = IOSSpacing.sm
            )
    )
}

@Composable
fun IOSSectionFooter(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = IOSSpacing.xl,
                vertical = IOSSpacing.sm
            )
    )
}

@Composable
fun IOSListGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            IOSSectionHeader(title = title)
        }
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = IOSSpacing.lg),
            shape = RoundedCornerShape(IOSRadius.lg),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(content = content)
        }
        
        if (footer != null) {
            IOSSectionFooter(text = footer)
        }
    }
}

@Composable
fun IOSListSection(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            IOSSectionHeader(title = title)
        }
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = IOSSpacing.lg),
            shape = RoundedCornerShape(IOSRadius.lg),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun IOSSpacer(
    height: Dp = IOSSpacing.md
) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun IOSDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 0.dp
) {
    HorizontalDivider(
        modifier = modifier.padding(start = startIndent),
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 0.5.dp
    )
}

fun LazyListScope.iosItem(
    content: @Composable () -> Unit
) {
    item { content() }
}

fun LazyListScope.iosSection(
    title: String? = null,
    footer: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    item {
        IOSSection(
            title = title,
            footer = footer,
            content = content
        )
    }
}

fun LazyListScope.iosListSection(
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    item {
        IOSListSection(
            title = title,
            content = content
        )
    }
}

fun LazyListScope.iosSpacer(
    height: Dp = IOSSpacing.md
) {
    item {
        IOSSpacer(height = height)
    }
}
