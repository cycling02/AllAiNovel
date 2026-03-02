package com.cycling.feature.outline.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.OutlineItem
import com.cycling.feature.outline.OutlineItemUiModel
import com.cycling.feature.outline.OutlineListEffect
import com.cycling.feature.outline.OutlineListIntent
import com.cycling.feature.outline.OutlineListViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineListScreen(
    bookId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToChapter: (Long) -> Unit,
    viewModel: OutlineListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiModels by viewModel.uiModels.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(bookId) {
        viewModel.setBookId(bookId)
    }
    
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is OutlineListEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is OutlineListEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                OutlineListEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is OutlineListEffect.NavigateToChapter -> {
                    onNavigateToChapter(effect.chapterId)
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            IOSNavBar(
                title = "大纲管理",
                onBack = { viewModel.navigateBack() }
            )
        },
        floatingActionButton = {
            IOSFAB(
                onClick = { viewModel.onIntent(OutlineListIntent.ShowAddDialog(null)) },
                text = "新增大纲"
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        when {
            state.isLoading -> {
                IOSFullScreenLoading()
            }
            state.outlineItems.isEmpty() -> {
                IOSEmptyState(
                    icon = Icons.Outlined.List,
                    title = "还没有大纲",
                    message = "创建大纲帮助你规划故事结构",
                    modifier = Modifier.padding(padding),
                    action = {
                        IOSButton(
                            text = "新增大纲",
                            onClick = { viewModel.onIntent(OutlineListIntent.ShowAddDialog(null)) },
                            icon = Icons.Default.Add,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = IOSSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
                ) {
                    item {
                        IOSSectionHeader(title = "大纲列表")
                    }
                    
                    items(uiModels, key = { it.item.id }) { uiModel ->
                        OutlineItemCard(
                            uiModel = uiModel,
                            onToggleExpand = { viewModel.onIntent(OutlineListIntent.ToggleExpand(uiModel.item.id)) },
                            onEdit = { viewModel.onIntent(OutlineListIntent.ShowEditDialog(uiModel.item)) },
                            onDelete = { viewModel.onIntent(OutlineListIntent.ShowDeleteDialog(uiModel.item)) },
                            onGenerateChapter = { viewModel.onIntent(OutlineListIntent.GenerateChapterFromOutline(uiModel.item)) }
                        )
                    }
                    
                    item {
                        IOSSpacer(height = IOSSpacing.xxxl)
                    }
                }
            }
        }
        
        if (state.showAddDialog) {
            AddOutlineItemDialog(
                parent = state.parentForNewItem,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideAddDialog) },
                onConfirm = { title, summary, parent ->
                    viewModel.onIntent(OutlineListIntent.AddOutlineItem(title, summary, parent))
                }
            )
        }
        
        if (state.showEditDialog && state.itemToEdit != null) {
            EditOutlineItemDialog(
                item = state.itemToEdit!!,
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideEditDialog) },
                onConfirm = { item, title, summary, status ->
                    viewModel.onIntent(OutlineListIntent.UpdateOutlineItem(item, title, summary, status))
                }
            )
        }
        
        if (state.showDeleteDialog && state.itemToDelete != null) {
            IOSConfirmDialog(
                visible = true,
                title = "删除大纲",
                message = "确定要删除「${state.itemToDelete!!.title}」吗？",
                onDismiss = { viewModel.onIntent(OutlineListIntent.HideDeleteDialog) },
                onConfirm = { viewModel.onIntent(OutlineListIntent.DeleteOutlineItem(false)) },
                isDestructive = true
            )
        }
        
        if (state.generatingChapterId != null) {
            IOSLoadingDialog(
                visible = true,
                message = "正在生成章节内容..."
            )
        }
    }
}

@Composable
private fun OutlineItemCard(
    uiModel: OutlineItemUiModel,
    onToggleExpand: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onGenerateChapter: () -> Unit
) {
    val item = uiModel.item
    
    IOSCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(IOSSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiModel.hasChildren) {
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (uiModel.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (uiModel.isExpanded) "收起" else "展开"
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(24.dp)) // 为了对齐
                }
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = (uiModel.level * 12).dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (item.summary.isNotBlank()) {
                        Text(
                            text = item.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onGenerateChapter,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "生成章节"
                    )
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "编辑"
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
