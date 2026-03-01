package com.cycling.feature.outline.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cycling.domain.model.OutlineItem

@Composable
fun AddOutlineItemDialog(
    parent: OutlineItem?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, summary: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    
    val dialogTitle = if (parent != null) {
        "添加子大纲 - ${parent.title}"
    } else {
        "添加顶级大纲"
    }
    
    val levelHint = if (parent != null) {
        val levelText = when (parent.level) {
            0 -> "章"
            1 -> "节"
            else -> "子项"
        }
        "将作为「${parent.title}」下的$levelText"
    } else {
        "将作为卷级别"
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            androidx.compose.foundation.layout.Column {
                Text(
                    text = levelHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题 *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                androidx.compose.foundation.layout.Spacer(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("简介") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title.trim(), summary.trim())
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("添加")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
