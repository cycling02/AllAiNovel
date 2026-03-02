package com.cycling.feature.character.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.cycling.core.ui.components.*
import com.cycling.domain.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCharacterDialog(
    bookId: Long,
    onDismiss: () -> Unit,
    onConfirm: (Character) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var personality by remember { mutableStateOf("") }
    var appearance by remember { mutableStateOf("") }
    var background by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("男", "女", "其他")

    IOSBottomSheet(
        visible = true,
        title = "添加角色",
        onDismiss = onDismiss,
        confirmText = "添加",
        confirmEnabled = name.isNotBlank(),
        onConfirm = {
            if (name.isNotBlank()) {
                val character = Character(
                    bookId = bookId,
                    name = name.trim(),
                    alias = alias.trim(),
                    gender = gender.trim(),
                    age = age.trim(),
                    personality = personality.trim(),
                    appearance = appearance.trim(),
                    background = background.trim(),
                    notes = notes.trim()
                )
                onConfirm(character)
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            IOSTextField(
                value = name,
                onValueChange = { name = it },
                label = "姓名 *",
                singleLine = true
            )

            IOSTextField(
                value = alias,
                onValueChange = { alias = it },
                label = "别名",
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                IOSTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = "性别",
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            IOSTextField(
                value = age,
                onValueChange = { age = it },
                label = "年龄",
                singleLine = true,
                keyboardType = KeyboardType.Text
            )

            IOSMultilineTextField(
                value = personality,
                onValueChange = { personality = it },
                label = "性格",
                minLines = 2,
                maxLines = 3
            )

            IOSMultilineTextField(
                value = appearance,
                onValueChange = { appearance = it },
                label = "外貌",
                minLines = 2,
                maxLines = 3
            )

            IOSMultilineTextField(
                value = background,
                onValueChange = { background = it },
                label = "背景",
                minLines = 2,
                maxLines = 4
            )

            IOSMultilineTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "备注",
                minLines = 2,
                maxLines = 3
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacterDialog(
    character: Character,
    onDismiss: () -> Unit,
    onConfirm: (Character) -> Unit
) {
    var name by remember { mutableStateOf(character.name) }
    var alias by remember { mutableStateOf(character.alias) }
    var gender by remember { mutableStateOf(character.gender) }
    var age by remember { mutableStateOf(character.age) }
    var personality by remember { mutableStateOf(character.personality) }
    var appearance by remember { mutableStateOf(character.appearance) }
    var background by remember { mutableStateOf(character.background) }
    var notes by remember { mutableStateOf(character.notes) }

    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("男", "女", "其他")

    IOSBottomSheet(
        visible = true,
        title = "编辑角色",
        onDismiss = onDismiss,
        confirmText = "保存",
        confirmEnabled = name.isNotBlank(),
        onConfirm = {
            if (name.isNotBlank()) {
                val updatedCharacter = character.copy(
                    name = name.trim(),
                    alias = alias.trim(),
                    gender = gender.trim(),
                    age = age.trim(),
                    personality = personality.trim(),
                    appearance = appearance.trim(),
                    background = background.trim(),
                    notes = notes.trim()
                )
                onConfirm(updatedCharacter)
            }
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            IOSTextField(
                value = name,
                onValueChange = { name = it },
                label = "姓名 *",
                singleLine = true
            )

            IOSTextField(
                value = alias,
                onValueChange = { alias = it },
                label = "别名",
                singleLine = true
            )

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                IOSTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = "性别",
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            IOSTextField(
                value = age,
                onValueChange = { age = it },
                label = "年龄",
                singleLine = true,
                keyboardType = KeyboardType.Text
            )

            IOSMultilineTextField(
                value = personality,
                onValueChange = { personality = it },
                label = "性格",
                minLines = 2,
                maxLines = 3
            )

            IOSMultilineTextField(
                value = appearance,
                onValueChange = { appearance = it },
                label = "外貌",
                minLines = 2,
                maxLines = 3
            )

            IOSMultilineTextField(
                value = background,
                onValueChange = { background = it },
                label = "背景",
                minLines = 2,
                maxLines = 4
            )

            IOSMultilineTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "备注",
                minLines = 2,
                maxLines = 3
            )
        }
    }
}
