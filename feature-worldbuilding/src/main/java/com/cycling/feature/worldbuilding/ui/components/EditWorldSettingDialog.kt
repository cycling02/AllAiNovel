package com.cycling.feature.worldbuilding.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cycling.domain.model.WorldSetting

@Composable
fun EditWorldSettingDialog(
    setting: WorldSetting,
    onDismiss: () -> Unit,
    onConfirm: (WorldSetting) -> Unit
) {
    var formData by remember { 
        mutableStateOf(WorldSettingFormData(
            name = setting.name,
            type = setting.type,
            description = setting.description,
            details = setting.details
        ))
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编辑世界观设定") },
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
                        val updatedSetting = setting.copy(
                            name = formData.name.trim(),
                            type = formData.type,
                            description = formData.description.trim(),
                            details = formData.details.trim()
                        )
                        onConfirm(updatedSetting)
                    }
                },
                enabled = formData.name.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
