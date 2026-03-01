package com.cycling.feature.chapter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.InputBottomSheet
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
                is ChapterListEffect.NavigateToChapterEdit -> {
                    onNavigateToChapterEdit(effect.chapterId)
                }
                is ChapterListEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                is ChapterListEffect.ShowUndoSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = "撤销",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.processIntent(ChapterListIntent.UndoDelete(effect.chapter))
                        }
                    }
                }
                is ChapterListEffect.ChapterRestored -> {
                    scope.launch {
                        snackbarHostState.showSnackbar("已恢复")
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("章节列表") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Description, contentDescription = "返回")
                    }
                },
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
            FloatingActionButton(
                onClick = { viewModel.processIntent(ChapterListIntent.ShowAddDialog) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加章节")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (chapters.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "还没有章节",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角按钮创建新章节",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chapters, key = { it.id }) { chapter ->
                    SwipeToDeleteChapterItem(
                        chapter = chapter,
                        onClick = { onNavigateToChapterEdit(chapter.id) },
                        onDelete = { viewModel.processIntent(ChapterListIntent.DeleteChapter(chapter)) }
                    )
                }
            }
        }

        InputBottomSheet(
            visible = state.showAddDialog,
            title = "创建新章",
            onDismiss = {
                viewModel.processIntent(ChapterListIntent.HideAddDialog)
                newChapterTitle = ""
            },
            confirmText = "创建",
            confirmEnabled = true,
            onConfirm = {
                viewModel.processIntent(ChapterListIntent.AddChapter(newChapterTitle))
                newChapterTitle = ""
            }
        ) {
            OutlinedTextField(
                value = newChapterTitle,
                onValueChange = { newChapterTitle = it },
                label = { Text("章节标题（可选）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("留空将自动生成章节号") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteChapterItem(
    chapter: Chapter,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    com.cycling.core.ui.components.SwipeToDeleteContainer(
        item = chapter,
        onDelete = onDelete
    ) {
        ChapterCard(chapter = chapter, onClick = onClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterCard(
    chapter: Chapter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getChapterStatusText(chapter.status),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    if (chapter.wordCount > 0) {
                        Text(
                            text = " · ${chapter.wordCount}字",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

private fun getChapterStatusText(status: ChapterStatus): String {
    return when (status) {
        ChapterStatus.DRAFT -> "草稿"
        ChapterStatus.PUBLISHED -> "已发布"
        ChapterStatus.ARCHIVED -> "已归档"
    }
}
