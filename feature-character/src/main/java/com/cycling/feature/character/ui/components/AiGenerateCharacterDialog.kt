package com.cycling.feature.character.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiGenerateCharacterDialog(
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onGenerate: (characterType: String?, gender: String?, description: String?, count: Int) -> Unit
) {
    var selectedCharacterType by remember { mutableStateOf<String?>(null) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }
    var count by remember { mutableIntStateOf(1) }
    
    var characterTypeExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }
    
    val characterTypeOptions = listOf("主角", "配角", "反派", "路人")
    val genderOptions = listOf("男", "女", "未知")

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("AI生成角色") },
        text = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ExposedDropdownMenuBox(
                    expanded = characterTypeExpanded,
                    onExpandedChange = { if (!isLoading) characterTypeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCharacterType ?: "不限",
                        onValueChange = {},
                        label = { Text("角色类型") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = characterTypeExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = characterTypeExpanded,
                        onDismissRequest = { characterTypeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("不限") },
                            onClick = {
                                selectedCharacterType = null
                                characterTypeExpanded = false
                            }
                        )
                        characterTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedCharacterType = option
                                    characterTypeExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { if (!isLoading) genderExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedGender ?: "不限",
                        onValueChange = {},
                        label = { Text("性别") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("不限") },
                            onClick = {
                                selectedGender = null
                                genderExpanded = false
                            }
                        )
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedGender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("简要描述") },
                    placeholder = { Text("描述角色特点或要求，如：性格开朗的少年剑客") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = count.toString(),
                    onValueChange = { 
                        val newValue = it.toIntOrNull()
                        if (newValue != null && newValue in 1..5) {
                            count = newValue
                        }
                    },
                    label = { Text("生成数量 (1-5)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onGenerate(
                        selectedCharacterType,
                        selectedGender,
                        description.ifBlank { null },
                        count
                    )
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    Text("生成中...")
                } else {
                    Text("生成")
                }
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
