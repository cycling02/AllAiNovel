package com.cycling.feature.chapter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.ChapterStatus
import com.cycling.feature.chapter.ChapterListEffect
import com.cycling.feature.chapter.ChapterListIntent
import com.cycling.feature.chapter.ChapterListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChapterEdit: (Long) -> Unit,
    onNavigateToOutline: (Long) -> Unit,
    onNavigateToCharacter: (Long) -> Unit,
    onNavigateToWorldBuilding: (Long) -> Unit,
    viewModel: ChapterListViewModel = hiltViewModel()
) {
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var newChapterTitle by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ChapterListEffect.NavigateToChapterEdit -> onNavigateToChapterEdit(effect.chapterId)
                is ChapterListEffect.ShowError -> {
                    scope.launch { snackbarHostState.showSnackbar(effect.message) }
                }
                is ChapterListEffect.ShowUndoSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = "撤销",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.processIntent(ChapterListIntent.UndoDelete(effect.chapter))
                        }
                    }
                }
                is ChapterListEffect.ChapterRestored -> {
                    scope.launch { snackbarHostState.showSnackbar("已恢复") }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            IOSNavBar(
                title = "章节列表",
                onBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { onNavigateToCharacter(state.bookId) }) {
                        Icon(Icons.Default.Person, contentDescription = "角色")
                    }
                    IconButton(onClick = { onNavigateToOutline(state.bookId) }) {
                        Icon(Icons.Default.List, contentDescription = "大纲")
                    }
                    IconButton(onClick = { onNavigateToWorldBuilding(state.bookId) }) {
                        Icon(Icons.Default.Public, contentDescription = "世界观")
                    }
                }
            )
        },
        floatingActionButton = {
            IOSFAB(onClick = { viewModel.processIntent(ChapterListIntent.ShowAddDialog) })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (chapters.isEmpty()) {
            IOSEmptyState(
                icon = Icons.Outlined.Description,
                title = "还没有章节",
                message = "创建第一章，开始你的故事",
                modifier = Modifier.padding(padding),
                action = {
                    IOSButton(
                        text = "新建章节",
                        onClick = { viewModel.processIntent(ChapterListIntent.ShowAddDialog) },
                        icon = Icons.Default.Add,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = IOSSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
            ) {
                item {
                    IOSSectionHeader(title = "全部章节")
                }
                
                items(chapters, key = { it.id }) { chapter ->
                    ChapterItem(
                        chapter = chapter,
                        onClick = { onNavigateToChapterEdit(chapter.id) },
                        onDelete = { viewModel.processIntent(ChapterListIntent.DeleteChapter(chapter)) }
                    )
                }
                
                iosSpacer(height = IOSSpacing.xl)
            }
        }

        IOSInputDialog(
            visible = state.showAddDialog,
            title = "创建新章",
            onDismiss = {
                viewModel.processIntent(ChapterListIntent.HideAddDialog)
                newChapterTitle = ""
            },
            onConfirm = {
                viewModel.processIntent(ChapterListIntent.AddChapter(it))
                newChapterTitle = ""
            },
            placeholder = "留空将自动生成章节号",
            label = "章节标题（可选）",
            confirmText = "创建",
            dismissText = "取消"
        )
    }
}

@Composable
private fun ChapterItem(
    chapter: Chapter,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    IOSSwipeToDelete(
        item = chapter,
        onDelete = onDelete
    ) {
        IOSListItemCard(
            icon = Icons.Outlined.Description,
            iconBackground = when (chapter.status) {
                ChapterStatus.DRAFT -> MaterialTheme.colorScheme.secondaryContainer
                ChapterStatus.PUBLISHED -> MaterialTheme.colorScheme.primaryContainer
                ChapterStatus.ARCHIVED -> MaterialTheme.colorScheme.surfaceVariant
            },
            iconTint = when (chapter.status) {
                ChapterStatus.DRAFT -> MaterialTheme.colorScheme.onSecondaryContainer
                ChapterStatus.PUBLISHED -> MaterialTheme.colorScheme.onPrimaryContainer
                ChapterStatus.ARCHIVED -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            title = chapter.title,
            subtitle = when {
                chapter.wordCount > 0 -> "${chapter.wordCount}字"
                else -> null
            },
            badge = when (chapter.status) {
                ChapterStatus.DRAFT -> "草稿"
                ChapterStatus.PUBLISHED -> "已发布"
                ChapterStatus.ARCHIVED -> "已归档"
            },
            onClick = onClick
        )
    }
}
