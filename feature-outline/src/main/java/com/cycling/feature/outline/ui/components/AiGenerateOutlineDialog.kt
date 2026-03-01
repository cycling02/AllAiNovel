package com.cycling.feature.outline.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AiGenerateOutlineDialog(
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onGenerate: (topic: String, summary: String, chapterCount: Int, levelCount: Int) -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var chapterCount by remember { mutableIntStateOf(20) }
    var selectedLevel by remember { mutableIntStateOf(2) }
    
    val levelOptions = listOf(
        1 to "1级（仅章节）",
        2 to "2级（卷-章）",
        3 to "3级（卷-章-节）"
    )
    
    val isValid = topic.isNotBlank() && summary.isNotBlank() && !isLoading
    
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("AI 生成大纲") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("主题/题材 *") },
                    placeholder = { Text("如：玄幻、都市、仙侠") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("故事简介 *") },
                    placeholder = { Text("描述核心故事线...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chapterCount.toString(),
                        onValueChange = { 
                            val num = it.toIntOrNull()
                            if (num != null && num > 0) {
                                chapterCount = num
                            } else if (it.isEmpty()) {
                                chapterCount = 0
                            }
                        },
                        label = { Text("目标章节数") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "大纲层级",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Column(
                    modifier = Modifier.selectableGroup()
                ) {
                    levelOptions.forEach { (level, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedLevel == level,
                                    onClick = { selectedLevel = level },
                                    enabled = !isLoading
                                )
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLevel == level,
                                onClick = null,
                                enabled = !isLoading
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValid) {
                        onGenerate(
                            topic.trim(),
                            summary.trim(),
                            chapterCount.coerceAtLeast(1),
                            selectedLevel
                        )
                    }
                },
                enabled = isValid
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                            .padding(end = 4.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text(if (isLoading) "生成中..." else "生成")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("取消")
            }
        }
    )
}
