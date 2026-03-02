package com.cycling.feature.ai.ui

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
import com.cycling.core.ui.components.*
import com.cycling.feature.ai.AiWritingIntent
import com.cycling.feature.ai.AiWritingMode
import com.cycling.feature.ai.AiWritingViewModel
import com.cycling.feature.ai.ui.components.PromptSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiWritingScreen(
    initialContext: String = "",
    onNavigateBack: () -> Unit,
    onApplyContent: (String) -> Unit,
    viewModel: AiWritingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val effect = viewModel.effect

    LaunchedEffect(Unit) {
        if (initialContext.isNotEmpty()) {
            viewModel.onIntent(AiWritingIntent.UpdateContext(initialContext))
        }
    }

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is com.cycling.feature.ai.AiWritingEffect.ShowToast -> {}
                is com.cycling.feature.ai.AiWritingEffect.NavigateBack -> onNavigateBack()
                is com.cycling.feature.ai.AiWritingEffect.ApplyContentToEditor -> onApplyContent(effect.content)
            }
        }
    }

    // 底部导航栏页面不使用 Scaffold，由 AppNavigation 统一管理
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        IOSNavBar(title = "AI 写作助手")
        
        // 内容区域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(IOSSpacing.lg)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.lg)
        ) {
            ModeSelector(
                selectedMode = state.selectedMode,
                onModeSelected = { mode ->
                    viewModel.onIntent(AiWritingIntent.SelectMode(mode))
                }
            )

            PromptSelector(
                prompts = state.prompts,
                selectedPrompt = state.selectedPrompt,
                onPromptSelected = { prompt ->
                    viewModel.onIntent(AiWritingIntent.SelectPrompt(prompt))
                }
            )

            IOSMultilineTextField(
                value = state.context,
                onValueChange = { viewModel.onIntent(AiWritingIntent.UpdateContext(it)) },
                label = "上下文内容",
                minLines = 5,
                maxLines = 10,
                enabled = !state.isLoading
            )

            IOSButton(
                text = getGenerateButtonText(state.selectedMode, state.selectedPrompt != null),
                onClick = { viewModel.onIntent(AiWritingIntent.GenerateContent()) },
                enabled = state.context.isNotBlank() && !state.isLoading,
                icon = Icons.Default.AutoAwesome,
                loading = state.isLoading
            )

            if (state.generatedResult.isNotEmpty()) {
                ResultCard(
                    result = state.generatedResult,
                    onApply = { viewModel.onIntent(AiWritingIntent.ApplyResult(it)) },
                    onRegenerate = { viewModel.onIntent(AiWritingIntent.Regenerate()) },
                    onClear = { viewModel.onIntent(AiWritingIntent.ClearResult) },
                    isLoading = state.isLoading
                )
            }

            state.error?.let { error ->
                IOSInlineError(
                    message = error,
                    onDismiss = { viewModel.onIntent(AiWritingIntent.DismissError) }
                )
            }
        }
    }
}

@Composable
private fun ModeSelector(
    selectedMode: AiWritingMode,
    onModeSelected: (AiWritingMode) -> Unit
) {
    IOSSection(title = "写作模式") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IOSSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
        ) {
            AiWritingMode.entries.forEach { mode ->
                IOSCompactButton(
                    text = getModeText(mode),
                    onClick = { onModeSelected(mode) },
                    style = if (selectedMode == mode) IOSButtonStyle.Primary else IOSButtonStyle.Secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ResultCard(
    result: String,
    onApply: (String) -> Unit,
    onRegenerate: () -> Unit,
    onClear: () -> Unit,
    isLoading: Boolean
) {
    IOSCard {
        Column(
            modifier = Modifier.padding(IOSSpacing.md),
            verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "生成结果",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(horizontalArrangement = Arrangement.spacedBy(IOSSpacing.xs)) {
                    IOSIconButton(
                        onClick = onRegenerate,
                        icon = Icons.Default.Refresh,
                        enabled = !isLoading
                    )
                    IOSIconButton(
                        onClick = onClear,
                        icon = Icons.Default.Close
                    )
                }
            }

            IOSDivider()

            Text(
                text = result,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .verticalScroll(rememberScrollState())
            )

            IOSButton(
                text = "应用内容",
                onClick = { onApply(result) },
                icon = Icons.Default.Check
            )
        }
    }
}

private fun getModeText(mode: AiWritingMode): String {
    return when (mode) {
        AiWritingMode.CONTINUE -> "续写"
        AiWritingMode.REWRITE -> "改写"
        AiWritingMode.EXPAND -> "扩写"
        AiWritingMode.POLISH -> "润色"
    }
}

private fun getGenerateButtonText(mode: AiWritingMode, hasSelectedPrompt: Boolean): String {
    val modeText = getModeText(mode)
    return if (hasSelectedPrompt) {
        "使用模板$modeText"
    } else {
        "开始$modeText"
    }
}
