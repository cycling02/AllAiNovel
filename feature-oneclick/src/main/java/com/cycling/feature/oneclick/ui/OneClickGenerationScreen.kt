package com.cycling.feature.oneclick.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting
import com.cycling.feature.oneclick.*
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneClickGenerationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEditor: (Long) -> Unit,
    viewModel: OneClickGenerationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { e ->
            when (e) {
                is OneClickGenerationEffect.NavigateBack -> onNavigateBack()
                is OneClickGenerationEffect.NavigateToEditor -> onNavigateToEditor(e.chapterId)
                is OneClickGenerationEffect.ShowToast -> {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                is OneClickGenerationEffect.ShowError -> {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("一键生成") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(OneClickGenerationIntent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isGenerating) {
            GenerationProgressContent(
                stage = state.generationStage,
                progress = state.progress,
                stageText = state.currentStageText,
                onCancel = { viewModel.onIntent(OneClickGenerationIntent.CancelGeneration) }
            )
        } else if (state.generationStage == GenerationStage.COMPLETED) {
            GenerationResultContent(
                state = state,
                onEditBook = { viewModel.onIntent(OneClickGenerationIntent.ShowBookEditDialog) },
                onEditCharacter = { viewModel.onIntent(OneClickGenerationIntent.ShowCharacterEditDialog(it)) },
                onEditWorldSetting = { viewModel.onIntent(OneClickGenerationIntent.ShowWorldSettingEditDialog(it)) },
                onEditOutlineItem = { viewModel.onIntent(OneClickGenerationIntent.ShowOutlineItemEditDialog(it)) },
                onChapterContentChange = { viewModel.onIntent(OneClickGenerationIntent.UpdateChapterContent(it)) },
                onApplyAll = { viewModel.onIntent(OneClickGenerationIntent.ApplyAllResults) },
                onRegenerate = { viewModel.onIntent(OneClickGenerationIntent.StartGeneration) }
            )
        } else {
            GenerationInputContent(
                state = state,
                onDescriptionChange = { viewModel.onIntent(OneClickGenerationIntent.UpdateDescription(it)) },
                onToggleCharacters = { viewModel.onIntent(OneClickGenerationIntent.ToggleGenerateCharacters(it)) },
                onToggleWorldSettings = { viewModel.onIntent(OneClickGenerationIntent.ToggleGenerateWorldSettings(it)) },
                onToggleOutline = { viewModel.onIntent(OneClickGenerationIntent.ToggleGenerateOutline(it)) },
                onToggleFirstChapter = { viewModel.onIntent(OneClickGenerationIntent.ToggleGenerateFirstChapter(it)) },
                onModelSelected = { viewModel.onIntent(OneClickGenerationIntent.SelectModel(it)) },
                onStartGeneration = { viewModel.onIntent(OneClickGenerationIntent.StartGeneration) }
            )
        }

        state.error?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.onIntent(OneClickGenerationIntent.ClearError) },
                title = { Text("错误") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { viewModel.onIntent(OneClickGenerationIntent.ClearError) }) {
                        Text("确定")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenerationInputContent(
    state: OneClickGenerationState,
    onDescriptionChange: (String) -> Unit,
    onToggleCharacters: (Boolean) -> Unit,
    onToggleWorldSettings: (Boolean) -> Unit,
    onToggleOutline: (Boolean) -> Unit,
    onToggleFirstChapter: (Boolean) -> Unit,
    onModelSelected: (ApiConfig) -> Unit,
    onStartGeneration: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.userDescription,
            onValueChange = onDescriptionChange,
            label = { Text("描述你的想法") },
            placeholder = { 
                Text("示例：\n《斗破苍穹》同人小说。\n主角：古无涯，古族大少爷...\n老婆洛璃，妹妹古薰儿...\n第一章写他们日常...") 
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp),
            maxLines = 10
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "生成选项",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.generateOptions.generateCharacters,
                        onClick = { onToggleCharacters(!state.generateOptions.generateCharacters) },
                        label = { Text("角色") }
                    )
                    FilterChip(
                        selected = state.generateOptions.generateWorldSettings,
                        onClick = { onToggleWorldSettings(!state.generateOptions.generateWorldSettings) },
                        label = { Text("世界观") }
                    )
                    FilterChip(
                        selected = state.generateOptions.generateOutline,
                        onClick = { onToggleOutline(!state.generateOptions.generateOutline) },
                        label = { Text("大纲") }
                    )
                    FilterChip(
                        selected = state.generateOptions.generateFirstChapter,
                        onClick = { onToggleFirstChapter(!state.generateOptions.generateFirstChapter) },
                        label = { Text("第一章") }
                    )
                }
            }
        }

        var modelExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = modelExpanded,
            onExpandedChange = { modelExpanded = it }
        ) {
            OutlinedTextField(
                value = state.selectedModel?.name ?: "选择模型",
                onValueChange = {},
                readOnly = true,
                label = { Text("AI模型") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = modelExpanded,
                onDismissRequest = { modelExpanded = false }
            ) {
                state.availableModels.forEach { model ->
                    DropdownMenuItem(
                        text = { Text(model.name) },
                        onClick = {
                            onModelSelected(model)
                            modelExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = onStartGeneration,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.userDescription.isNotBlank() && 
                      state.selectedModel != null && 
                      state.generateOptions.hasAnyOption
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("开始生成")
        }

        if (state.availableModels.isEmpty()) {
            Text(
                text = "请先在设置中配置AI模型",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun GenerationProgressContent(
    stage: GenerationStage,
    progress: Float,
    stageText: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(120.dp),
            strokeWidth = 8.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stageText,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onCancel,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.Cancel, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("取消生成")
        }
    }
}

@Composable
private fun GenerationResultContent(
    state: OneClickGenerationState,
    onEditBook: () -> Unit,
    onEditCharacter: (Character) -> Unit,
    onEditWorldSetting: (WorldSetting) -> Unit,
    onEditOutlineItem: (OutlineItem) -> Unit,
    onChapterContentChange: (String) -> Unit,
    onApplyAll: () -> Unit,
    onRegenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        state.generatedBook?.let { book ->
            ResultSection(
                title = "📚 书籍信息",
                onEdit = onEditBook
            ) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text(book.description, style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (state.generatedCharacters.isNotEmpty()) {
            ResultSection(
                title = "👤 角色 (${state.generatedCharacters.size}个)"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.generatedCharacters.forEach { character ->
                        CharacterCard(
                            character = character,
                            onClick = { onEditCharacter(character) }
                        )
                    }
                }
            }
        }

        if (state.generatedWorldSettings.isNotEmpty()) {
            ResultSection(
                title = "🌍 世界观 (${state.generatedWorldSettings.size}个)"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.generatedWorldSettings.forEach { setting ->
                        WorldSettingCard(
                            worldSetting = setting,
                            onClick = { onEditWorldSetting(setting) }
                        )
                    }
                }
            }
        }

        if (state.generatedOutline.isNotEmpty()) {
            ResultSection(
                title = "📋 大纲"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    state.generatedOutline.forEach { item ->
                        OutlineItemCard(
                            outlineItem = item,
                            onClick = { onEditOutlineItem(item) }
                        )
                    }
                }
            }
        }

        if (state.generatedChapterContent.isNotBlank()) {
            ResultSection(
                title = "📝 第一章内容 (${state.generatedChapterContent.length}字)"
            ) {
                OutlinedTextField(
                    value = state.generatedChapterContent,
                    onValueChange = onChapterContentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 400.dp),
                    maxLines = 15
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onRegenerate,
                modifier = Modifier.weight(1f)
            ) {
                Text("重新生成")
            }
            Button(
                onClick = onApplyAll,
                modifier = Modifier.weight(1f)
            ) {
                Text("全部应用")
            }
        }
    }
}

@Composable
private fun ResultSection(
    title: String,
    onEdit: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                onEdit?.let {
                    TextButton(onClick = it) {
                        Text("编辑")
                    }
                }
            }
            HorizontalDivider()
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleSmall
            )
            if (character.personality.isNotBlank()) {
                Text(
                    text = "性格: ${character.personality}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (character.background.isNotBlank()) {
                Text(
                    text = character.background.take(100) + if (character.background.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorldSettingCard(
    worldSetting: WorldSetting,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "[${worldSetting.type.displayName}] ${worldSetting.name}",
                style = MaterialTheme.typography.titleSmall
            )
            if (worldSetting.description.isNotBlank()) {
                Text(
                    text = worldSetting.description.take(100) + if (worldSetting.description.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlineItemCard(
    outlineItem: OutlineItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (outlineItem.level * 16).dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = outlineItem.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
