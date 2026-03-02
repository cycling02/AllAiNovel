package com.cycling.feature.editor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cycling.feature.editor.model.ChapterEditEffect
import com.cycling.feature.editor.model.ChapterEditIntent
import com.cycling.feature.editor.viewmodel.ChapterEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterEditScreen(
    onNavigateBack: () -> Unit,
    isEditable: Boolean = true,
    viewModel: ChapterEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ChapterEditEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is ChapterEditEffect.NavigateBack -> onNavigateBack()
                is ChapterEditEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
    
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.handleIntent(ChapterEditIntent.ClearError)
        }
    }
    
    Scaffold(
        topBar = {
            IOSNavBar(
                title = if (state.isLoading) "加载中..." else state.title,
                onBack = onNavigateBack,
                actions = {
                    if (state.hasUnsavedChanges && state.isEditable) {
                        TextButton(onClick = { viewModel.handleIntent(ChapterEditIntent.SaveChapter) }) {
                            Text("保存")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                IOSFullScreenLoading()
            } else {
                OutlinedTextField(
                    value = state.content,
                    onValueChange = { 
                        if (!state.isStreaming && state.isEditable) {
                            viewModel.handleIntent(ChapterEditIntent.UpdateContent(it))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = IOSSpacing.lg),
                    placeholder = { 
                        Text(
                            "开始写作...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        ) 
                    },
                    isError = state.error != null,
                    readOnly = !state.isEditable || state.isStreaming,
                    shape = RoundedCornerShape(IOSRadius.lg),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    trailingIcon = {
                        if (state.isStreaming) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(IOSSize.iconSm),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                )
            }
            
            EditorBottomBar(
                state = state,
                onToggleContext = { viewModel.handleIntent(ChapterEditIntent.ToggleUseContext) },
                onContinueWriting = { viewModel.handleIntent(ChapterEditIntent.ContinueWriting) },
                onStopStreaming = { viewModel.handleIntent(ChapterEditIntent.StopStreaming) }
            )
        }
    }
}

@Composable
private fun EditorBottomBar(
    state: com.cycling.feature.editor.model.ChapterEditState,
    onToggleContext: () -> Unit,
    onContinueWriting: () -> Unit,
    onStopStreaming: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = IOSSpacing.lg, vertical = IOSSpacing.md)
        ) {
            if (!state.isEditable) {
                Text(
                    text = "只读模式",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val bookContext = state.bookContext
                    val hasContext = bookContext != null && 
                        (bookContext.characters.isNotEmpty() || 
                         bookContext.worldSettings.isNotEmpty() || 
                         bookContext.outlineItems.isNotEmpty())

                    if (hasContext) {
                        IOSCompactButton(
                            text = if (state.useContext) "上下文: 开" else "上下文: 关",
                            onClick = onToggleContext,
                            style = if (state.useContext) IOSButtonStyle.Primary else IOSButtonStyle.Secondary,
                            icon = Icons.Default.AutoAwesome
                        )
                    }

                    when {
                        state.isStreaming -> {
                            Text(
                                text = "AI正在生成...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        state.isSaving -> {
                            Text(
                                text = "保存中...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        !state.hasUnsavedChanges && state.content.isNotEmpty() -> {
                            Text(
                                text = "已保存",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                IOSSpacer(height = IOSSpacing.md)
                
                if (state.isStreaming) {
                    IOSButton(
                        text = "停止生成",
                        onClick = onStopStreaming,
                        style = IOSButtonStyle.Error,
                        icon = Icons.Default.Stop
                    )
                } else {
                    IOSButton(
                        text = if (state.isAiLoading) "AI续写中..." else "AI续写",
                        onClick = onContinueWriting,
                        enabled = !state.isAiLoading && state.apiConfig != null,
                        icon = Icons.Default.AutoAwesome,
                        loading = state.isAiLoading
                    )
                }
                
                if (state.apiConfig == null) {
                    IOSSpacer(height = IOSSpacing.sm)
                    Text(
                        text = "请先在设置中配置API",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
