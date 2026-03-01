package com.cycling.feature.outline.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.feature.outline.OutlineListEffect
import com.cycling.feature.outline.OutlineListIntent
import com.cycling.feature.outline.OutlineListViewModel
import com.cycling.feature.outline.ui.components.AddOutlineItemDialog
import com.cycling.feature.outline.ui.components.AiGenerateOutlineDialog
import com.cycling.feature.outline.ui.components.AiOutlinePreviewDialog
import com.cycling.feature.outline.ui.components.DeleteOutlineItemDialog
import com.cycling.feature.outline.ui.components.EditOutlineItemDialog
import com.cycling.feature.outline.ui.components.OutlineItemCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineListScreen(
    bookId: Long,
    onNavigateBack: () -> Unit,
    viewModel: OutlineListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect
    
    val uiModels = viewModel.getOutlineUiModels()
    
    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is OutlineListEffect.ShowToast -> {
                }
                is OutlineListEffect.ShowError -> {
                }
                OutlineListEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("大纲管理") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(OutlineListIntent.ShowAiGenerateDialog) }) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI生成大纲"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(OutlineListIntent.ShowAddDialog(null)) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加顶级大纲")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.isEmpty -> {
                    EmptyOutlineList(
                        onAiGenerateClick = { viewModel.onIntent(OutlineListIntent.ShowAiGenerateDialog) }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiModels, key = { it.item.id }) { uiModel ->
                            OutlineItemCard(
                                uiModel = uiModel,
                                onToggleExpand = {
                                    viewModel.onIntent(OutlineListIntent.ToggleExpand(uiModel.item.id))
                                },
                                onAddChild = {
                                    viewModel.onIntent(OutlineListIntent.ShowAddDialog(uiModel.item))
                                },
                                onEdit = {
                                    viewModel.onIntent(OutlineListIntent.ShowEditDialog(uiModel.item))
                                },
                                onDelete = {
                                    viewModel.onIntent(OutlineListIntent.ShowDeleteDialog(uiModel.item))
                                },
                                canAddChild = viewModel.canAddChild(uiModel.item)
                            )
                        }
                    }
                }
            }
        }
        
        if (state.showAddDialog) {
            AddOutlineItemDialog(
                parent = state.parentForNewItem,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideAddDialog) },
                onConfirm = { title, summary ->
                    viewModel.onIntent(
                        OutlineListIntent.AddOutlineItem(
                            title = title,
                            summary = summary,
                            parent = state.parentForNewItem
                        )
                    )
                }
            )
        }
        
        if (state.showEditDialog && state.itemToEdit != null) {
            EditOutlineItemDialog(
                item = state.itemToEdit!!,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideEditDialog) },
                onConfirm = { title, summary, status ->
                    viewModel.onIntent(
                        OutlineListIntent.UpdateOutlineItem(
                            item = state.itemToEdit!!,
                            title = title,
                            summary = summary,
                            status = status
                        )
                    )
                }
            )
        }
        
        if (state.showDeleteDialog && state.itemToDelete != null) {
            DeleteOutlineItemDialog(
                item = state.itemToDelete!!,
                hasChildren = viewModel.hasChildren(state.itemToDelete!!.id),
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideDeleteDialog) },
                onConfirm = { deleteChildren ->
                    viewModel.onIntent(OutlineListIntent.DeleteOutlineItem(deleteChildren))
                }
            )
        }
        
        if (state.showAiGenerateDialog) {
            AiGenerateOutlineDialog(
                isLoading = state.isAiGenerating,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideAiGenerateDialog) },
                onGenerate = { topic, summary, chapterCount, levelCount ->
                    viewModel.onIntent(
                        OutlineListIntent.GenerateOutline(
                            topic = topic,
                            summary = summary,
                            chapterCount = chapterCount,
                            levelCount = levelCount
                        )
                    )
                }
            )
        }
        
        if (state.showAiPreviewDialog && state.aiGeneratedOutline != null) {
            AiOutlinePreviewDialog(
                outlineItems = state.aiGeneratedOutline!!,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideAiPreviewDialog) },
                onApply = { viewModel.onIntent(OutlineListIntent.ApplyAiOutline) }
            )
        }
    }
}

@Composable
private fun EmptyOutlineList(
    onAiGenerateClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "还没有大纲",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "点击右下角按钮创建大纲",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onAiGenerateClick
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("AI 生成大纲")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "支持三级结构：卷 → 章 → 节",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
