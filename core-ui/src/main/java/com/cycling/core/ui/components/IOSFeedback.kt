package com.cycling.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun IOSLoading(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
            
            if (!message.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(IOSSpacing.md))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun IOSFullScreenLoading(
    message: String? = null
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        IOSLoading(message = message)
    }
}

@Composable
fun IOSEmptyState(
    icon: ImageVector = Icons.Outlined.Inbox,
    title: String,
    message: String? = null,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    secondaryAction: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(IOSSpacing.lg))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            if (!message.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(IOSSpacing.sm))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            
            if (action != null || secondaryAction != null) {
                Spacer(modifier = Modifier.height(IOSSpacing.xl))
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
                ) {
                    action?.invoke()
                    secondaryAction?.invoke()
                }
            }
        }
    }
}

@Composable
fun IOSErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    IOSEmptyState(
        icon = Icons.Outlined.ErrorOutline,
        title = "出错了",
        message = message,
        modifier = modifier,
        action = if (onRetry != null) {
            { IOSButton(text = "重试", onClick = onRetry) }
        } else null
    )
}

@Composable
fun IOSInlineError(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    IOSCard(
        modifier = modifier,
        padding = IOSSpacing.md
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(IOSSpacing.sm))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            
            if (onRetry != null || onDismiss != null) {
                Spacer(modifier = Modifier.height(IOSSpacing.md))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
                ) {
                    if (onRetry != null) {
                        IOSButton(
                            text = "重试",
                            onClick = onRetry,
                            style = IOSButtonStyle.Error
                        )
                    }
                    if (onDismiss != null) {
                        IOSButton(
                            text = "关闭",
                            onClick = onDismiss,
                            style = IOSButtonStyle.Secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IOSSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = modifier.padding(IOSSpacing.md),
        shape = MaterialTheme.shapes.medium,
        containerColor = if (isSystemInDarkTheme()) {
            Color(0xFF2C2C2E)
        } else {
            Color(0xFF1C1C1E)
        },
        contentColor = Color.White,
        action = if (actionLabel != null && onAction != null) {
            {
                TextButton(onClick = onAction) {
                    Text(
                        text = actionLabel,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else null,
        dismissAction = {
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(IOSSize.iconSm)
                )
            }
        }
    ) {
        Text(text = message)
    }
}

@Composable
fun IOSUndoSnackbar(
    message: String,
    onUndo: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    IOSSnackbar(
        message = message,
        modifier = modifier,
        actionLabel = "撤销",
        onAction = onUndo,
        onDismiss = onDismiss
    )
}
