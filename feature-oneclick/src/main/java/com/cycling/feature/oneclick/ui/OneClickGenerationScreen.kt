package com.cycling.feature.oneclick.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Character
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.WorldSetting
import com.cycling.feature.oneclick.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

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
            IOSNavBar(
                title = "一键生成",
                onBack = { viewModel.onIntent(OneClickGenerationIntent.NavigateBack) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                IOSConfirmDialog(
                    visible = true,
                    title = "错误",
                    message = error,
                    onDismiss = { viewModel.onIntent(OneClickGenerationIntent.ClearError) },
                    onConfirm = { viewModel.onIntent(OneClickGenerationIntent.ClearError) },
                    confirmText = "确定",
                    dismissText = "关闭"
                )
            }
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
            .padding(IOSSpacing.lg)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(IOSSpacing.lg)
    ) {
        IOSMultilineTextField(
            value = state.userDescription,
            onValueChange = onDescriptionChange,
            label = "描述你的想法",
            placeholder = "示例：\n《斗破苍穹》同人小说。\n主角：古无涯，古族大少爷...\n老婆洛璃，妹妹古薰儿...\n第一章写他们日常...",
            minLines = 6,
            maxLines = 10
        )

        IOSCard {
            Column(
                modifier = Modifier.padding(IOSSpacing.md),
                verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
            ) {
                Text(
                    text = "生成选项",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
                ) {
                    IOSCompactButton(
                        text = "角色",
                        onClick = { onToggleCharacters(!state.generateOptions.generateCharacters) },
                        style = if (state.generateOptions.generateCharacters) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                    IOSCompactButton(
                        text = "世界观",
                        onClick = { onToggleWorldSettings(!state.generateOptions.generateWorldSettings) },
                        style = if (state.generateOptions.generateWorldSettings) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                    IOSCompactButton(
                        text = "大纲",
                        onClick = { onToggleOutline(!state.generateOptions.generateOutline) },
                        style = if (state.generateOptions.generateOutline) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                    IOSCompactButton(
                        text = "第一章",
                        onClick = { onToggleFirstChapter(!state.generateOptions.generateFirstChapter) },
                        style = if (state.generateOptions.generateFirstChapter) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                }
            }
        }

        var modelExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = modelExpanded,
            onExpandedChange = { modelExpanded = it }
        ) {
            IOSTextField(
                value = state.selectedModel?.name ?: "选择模型",
                onValueChange = {},
                readOnly = true,
                label = "AI模型",
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

        IOSButton(
            text = "开始生成",
            onClick = onStartGeneration,
            enabled = state.userDescription.isNotBlank() && 
                      state.selectedModel != null && 
                      state.generateOptions.hasAnyOption,
            icon = Icons.Default.AutoAwesome
        )

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
            .padding(IOSSpacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(120.dp),
            strokeWidth = 8.dp
        )

        IOSSpacer(height = IOSSpacing.xl)

        Text(
            text = stageText,
            style = MaterialTheme.typography.titleMedium
        )

        IOSSpacer(height = IOSSpacing.sm)

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        IOSSpacer(height = IOSSpacing.xxxl)

        IOSButton(
            text = "取消生成",
            onClick = onCancel,
            style = IOSButtonStyle.Error,
            icon = Icons.Default.Cancel
        )
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
            .padding(IOSSpacing.lg)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(IOSSpacing.lg)
    ) {
        state.generatedBook?.let { book ->
            IOSCard(onClick = onEditBook) {
                Text(
                    text = "书籍信息",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                IOSSpacer(height = IOSSpacing.sm)
                Text(book.title, style = MaterialTheme.typography.bodyLarge)
                Text(book.description, style = MaterialTheme.typography.bodySmall)
            }
        }

        if (state.generatedCharacters.isNotEmpty()) {
            IOSSection(title = "角色 (${state.generatedCharacters.size}个)") {
                state.generatedCharacters.forEach { character ->
                    IOSListItem(
                        icon = Icons.Default.Person,
                        title = character.name,
                        subtitle = character.personality.take(50),
                        onClick = { onEditCharacter(character) },
                        showChevron = true
                    )
                }
            }
        }

        if (state.generatedWorldSettings.isNotEmpty()) {
            IOSSection(title = "世界观 (${state.generatedWorldSettings.size}个)") {
                state.generatedWorldSettings.forEach { setting ->
                    IOSListItem(
                        icon = Icons.Default.Public,
                        title = "[${setting.type.displayName}] ${setting.name}",
                        subtitle = setting.description.take(50),
                        onClick = { onEditWorldSetting(setting) },
                        showChevron = true
                    )
                }
            }
        }

        if (state.generatedOutline.isNotEmpty()) {
            IOSSection(title = "大纲") {
                state.generatedOutline.forEach { item ->
                    IOSListItem(
                        title = item.title,
                        onClick = { onEditOutlineItem(item) },
                        showChevron = true
                    )
                }
            }
        }

        if (state.generatedChapterContent.isNotBlank()) {
            IOSSection(title = "第一章内容 (${state.generatedChapterContent.length}字)") {
                IOSMultilineTextField(
                    value = state.generatedChapterContent,
                    onValueChange = onChapterContentChange,
                    minLines = 5,
                    maxLines = 10
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            IOSButton(
                text = "重新生成",
                onClick = onRegenerate,
                style = IOSButtonStyle.Secondary,
                modifier = Modifier.weight(1f)
            )
            IOSButton(
                text = "全部应用",
                onClick = onApplyAll,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
