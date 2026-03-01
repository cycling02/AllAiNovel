package com.cycling.feature.editor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                is ChapterEditEffect.NavigateBack -> {
                    onNavigateBack()
                }
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
            TopAppBar(
                title = { Text(if (state.isLoading) "加载中..." else state.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (state.hasUnsavedChanges && state.isEditable) {
                        IconButton(onClick = { viewModel.handleIntent(ChapterEditIntent.SaveChapter) }) {
                            Icon(Icons.Default.Save, contentDescription = "保存")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
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
                        .padding(16.dp),
                    placeholder = { Text("开始写作...") },
                    isError = state.error != null,
                    readOnly = !state.isEditable || state.isStreaming,
                    trailingIcon = {
                        if (state.isStreaming) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                )
            }
            
            if (!state.isEditable) {
                Text(
                    text = "只读模式",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    val bookContext = state.bookContext
                    val hasContext = bookContext != null && 
                        (bookContext.characters.isNotEmpty() || 
                         bookContext.worldSettings.isNotEmpty() || 
                         bookContext.outlineItems.isNotEmpty())

                    if (hasContext) {
                        FilterChip(
                            selected = state.useContext,
                            onClick = { viewModel.handleIntent(ChapterEditIntent.ToggleUseContext) },
                            label = {
                                Text(
                                    if (state.useContext) "上下文感知: 开" else "上下文感知: 关"
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (state.isStreaming) {
                        Text(
                            text = "AI正在生成...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else if (state.isSaving) {
                        Text(
                            text = "保存中...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    } else if (!state.hasUnsavedChanges && state.content.isNotEmpty()) {
                        Text(
                            text = "已保存",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                
                if (state.isStreaming) {
                    Button(
                        onClick = { viewModel.handleIntent(ChapterEditIntent.StopStreaming) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("停止生成")
                    }
                } else {
                    Button(
                        onClick = { viewModel.handleIntent(ChapterEditIntent.ContinueWriting) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        enabled = !state.isAiLoading && state.apiConfig != null
                    ) {
                        if (state.isAiLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI续写中...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI续写")
                        }
                    }
                }
                
                if (state.apiConfig == null) {
                    Text(
                        text = "请先在设置中配置API",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
