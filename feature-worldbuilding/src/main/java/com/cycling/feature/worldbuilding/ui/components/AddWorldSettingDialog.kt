package com.cycling.feature.worldbuilding.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cycling.domain.model.SettingType
import com.cycling.domain.model.WorldSetting

@Composable
fun AddWorldSettingDialog(
    bookId: Long,
    onDismiss: () -> Unit,
    onConfirm: (WorldSetting) -> Unit
) {
    var formData by remember { 
        mutableStateOf(WorldSettingFormData(
            name = "",
            type = SettingType.LOCATION,
            description = "",
            details = ""
        ))
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加世界观设定") },
        text = {
            WorldSettingFormContent(
                formData = formData,
                onFormDataChange = { formData = it }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (formData.name.isNotBlank()) {
                        val setting = WorldSetting(
                            bookId = bookId,
                            type = formData.type,
                            name = formData.name.trim(),
                            description = formData.description.trim(),
                            details = formData.details.trim()
                        )
                        onConfirm(setting)
                    }
                },
                enabled = formData.name.isNotBlank()
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
