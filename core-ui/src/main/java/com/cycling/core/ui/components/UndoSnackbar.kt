package com.cycling.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class UndoSnackbarVisuals(
    override val message: String,
    override val actionLabel: String = "撤销",
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

@Composable
fun UndoSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.padding(16.dp),
        snackbar = { snackbarData ->
            UndoSnackbar(snackbarData)
        }
    )
}

@Composable
fun UndoSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
        action = {
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { snackbarData.performAction() }
                ) {
                    Text(
                        text = actionLabel,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
            }
        },
        dismissAction = {
            if (snackbarData.visuals.withDismissAction) {
                TextButton(
                    onClick = { snackbarData.dismiss() }
                ) {
                    Text(
                        text = "关闭",
                        color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    ) {
        Text(text = snackbarData.visuals.message)
    }
}

suspend fun SnackbarHostState.showUndoSnackbar(
    message: String,
    actionLabel: String = "撤销",
    duration: SnackbarDuration = SnackbarDuration.Short
): SnackbarResult {
    return showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = false
    )
}
