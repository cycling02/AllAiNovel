package com.cycling.feature.outline.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cycling.core.ui.components.*
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOutlineItemDialog(
    parent: OutlineItem?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, OutlineItem?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }

    IOSBottomSheet(
        visible = true,
        title = if (parent != null) "添加子大纲" else "添加大纲",
        onDismiss = onDismiss,
        confirmText = "添加",
        confirmEnabled = title.isNotBlank(),
        onConfirm = {
            if (title.isNotBlank()) {
                onConfirm(title.trim(), summary.trim(), parent)
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            if (parent != null) {
                Text(
                    text = "父级: ${parent.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IOSTextField(
                value = title,
                onValueChange = { title = it },
                label = "标题 *",
                singleLine = true
            )

            IOSMultilineTextField(
                value = summary,
                onValueChange = { summary = it },
                label = "摘要",
                minLines = 3,
                maxLines = 5
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditOutlineItemDialog(
    item: OutlineItem,
    onDismiss: () -> Unit,
    onConfirm: (OutlineItem, String, String, OutlineStatus) -> Unit
) {
    var title by remember { mutableStateOf(item.title) }
    var summary by remember { mutableStateOf(item.summary) }
    var status by remember { mutableStateOf(item.status) }
    var statusExpanded by remember { mutableStateOf(false) }

    IOSBottomSheet(
        visible = true,
        title = "编辑大纲",
        onDismiss = onDismiss,
        confirmText = "保存",
        confirmEnabled = title.isNotBlank(),
        onConfirm = {
            if (title.isNotBlank()) {
                onConfirm(item, title.trim(), summary.trim(), status)
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            IOSTextField(
                value = title,
                onValueChange = { title = it },
                label = "标题 *",
                singleLine = true
            )

            IOSMultilineTextField(
                value = summary,
                onValueChange = { summary = it },
                label = "摘要",
                minLines = 3,
                maxLines = 5
            )

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = it }
            ) {
                IOSTextField(
                    value = getStatusText(status),
                    onValueChange = {},
                    label = "状态",
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    OutlineStatus.entries.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(getStatusText(s)) },
                            onClick = {
                                status = s
                                statusExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun getStatusText(status: OutlineStatus): String {
    return when (status) {
        OutlineStatus.PENDING -> "待写作"
        OutlineStatus.WRITING -> "写作中"
        OutlineStatus.COMPLETED -> "已完成"
        OutlineStatus.ABANDONED -> "已废弃"
    }
}
