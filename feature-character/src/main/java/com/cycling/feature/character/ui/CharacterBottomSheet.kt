package com.cycling.feature.character.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cycling.core.ui.components.InputBottomSheet
import com.cycling.domain.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCharacterBottomSheet(
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

    InputBottomSheet(
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
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名 *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("别名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("性别") },
                    readOnly = false,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("年龄") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = personality,
                onValueChange = { personality = it },
                label = { Text("性格") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = appearance,
                onValueChange = { appearance = it },
                label = { Text("外貌") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = background,
                onValueChange = { background = it },
                label = { Text("背景") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("备注") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCharacterBottomSheet(
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

    InputBottomSheet(
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
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名 *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("别名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("性别") },
                    readOnly = false,
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

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("年龄") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = personality,
                onValueChange = { personality = it },
                label = { Text("性格") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = appearance,
                onValueChange = { appearance = it },
                label = { Text("外貌") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = background,
                onValueChange = { background = it },
                label = { Text("背景") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("备注") },
                minLines = 2,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
