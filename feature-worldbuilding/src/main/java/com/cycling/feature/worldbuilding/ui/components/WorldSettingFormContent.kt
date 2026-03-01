package com.cycling.feature.worldbuilding.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cycling.domain.model.SettingType

data class WorldSettingFormData(
    val name: String,
    val type: SettingType,
    val description: String,
    val details: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldSettingFormContent(
    formData: WorldSettingFormData,
    onFormDataChange: (WorldSettingFormData) -> Unit,
    modifier: Modifier = Modifier
) {
    var typeExpanded by remember { mutableStateOf(false) }
    
    androidx.compose.foundation.layout.Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = formData.name,
            onValueChange = { onFormDataChange(formData.copy(name = it)) },
            label = { Text("名称 *") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        ExposedDropdownMenuBox(
            expanded = typeExpanded,
            onExpandedChange = { typeExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = formData.type.displayName,
                onValueChange = {},
                label = { Text("类型") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false }
            ) {
                SettingType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.displayName) },
                        onClick = {
                            onFormDataChange(formData.copy(type = type))
                            typeExpanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = formData.description,
            onValueChange = { onFormDataChange(formData.copy(description = it)) },
            label = { Text("简短描述") },
            minLines = 2,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = formData.details,
            onValueChange = { onFormDataChange(formData.copy(details = it)) },
            label = { Text("详细说明") },
            minLines = 4,
            maxLines = 8,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
