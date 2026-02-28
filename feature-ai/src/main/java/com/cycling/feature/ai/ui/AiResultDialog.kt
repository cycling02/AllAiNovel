package com.cycling.feature.ai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AiResultDialog(
    result: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onApply: (String) -> Unit,
    onRegenerate: () -> Unit,
    onCopy: (String) -> Unit
) {
    var editedResult by remember(result) { mutableStateOf(result) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "AI 生成结果",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = onDismiss) {
                        Text("×", style = MaterialTheme.typography.titleLarge)
                    }
                }

                Divider()

                OutlinedTextField(
                    value = editedResult,
                    onValueChange = { editedResult = it },
                    label = { Text("生成内容（可编辑）") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    minLines = 10,
                    maxLines = 20
                )

                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onRegenerate,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("重新生成")
                    }

                    OutlinedButton(
                        onClick = { onCopy(editedResult) },
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("复制")
                    }
                }

                Button(
                    onClick = { onApply(editedResult) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && editedResult.isNotBlank()
                ) {
                    Text("应用到编辑器")
                }
            }
        }
    }
}

@Composable
fun AiResultPreviewCard(
    result: String,
    maxLines: Int = 3,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "AI 生成结果",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = result,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "点击查看详情",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
