package com.cycling.feature.outline.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cycling.domain.model.OutlineItem

@Composable
fun DeleteOutlineItemDialog(
    item: OutlineItem,
    hasChildren: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (deleteChildren: Boolean) -> Unit
) {
    if (hasChildren) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("删除大纲项") },
            text = {
                Column {
                    Text(
                        text = "「${item.title}」包含子大纲项，请选择删除方式：",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "• 仅删除此项：子项将上移一级",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• 删除此项及所有子项：将永久删除所有内容",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm(true) }
                ) {
                    Text(
                        text = "删除全部",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                Column {
                    TextButton(
                        onClick = { onConfirm(false) }
                    ) {
                        Text("仅删除此项")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("删除大纲项") },
            text = {
                Text("确定要删除「${item.title}」吗？")
            },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm(false) }
                ) {
                    Text(
                        text = "删除",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}
