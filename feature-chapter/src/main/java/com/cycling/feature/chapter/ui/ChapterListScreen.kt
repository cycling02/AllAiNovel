package com.cycling.feature.chapter.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.ChapterStatus
import com.cycling.feature.chapter.ChapterListViewModel
import com.cycling.feature.chapter.ChapterListIntent
import com.cycling.feature.chapter.ChapterListEffect

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
    val chapters by viewModel.chapters.collectAsState()
    val state by viewModel.state.collectAsState()

    var newChapterTitle by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ChapterListEffect.NavigateToChapterEdit -> {
                    onNavigateToChapterEdit(effect.chapterId)
                }
                is ChapterListEffect.ShowError -> {
                    // Handle error
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
        }
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
                    ChapterItem(
                        chapter = chapter,
                        onClick = { onNavigateToChapterEdit(chapter.id) },
                        onDelete = { viewModel.processIntent(ChapterListIntent.ShowDeleteDialog(chapter)) }
                    )
                }
            }
        }

        if (state.showAddDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.processIntent(ChapterListIntent.HideAddDialog) },
                title = { Text("创建新章") },
                text = {
                    OutlinedTextField(
                        value = newChapterTitle,
                        onValueChange = { newChapterTitle = it },
                        label = { Text("章节标题（可选）") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("留空将自动生成章节号") }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.processIntent(ChapterListIntent.AddChapter(newChapterTitle))
                            newChapterTitle = ""
                        }
                    ) {
                        Text("创建")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.processIntent(ChapterListIntent.HideAddDialog) }) {
                        Text("取消")
                    }
                }
            )
        }

        if (state.showDeleteDialog && state.chapterToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.processIntent(ChapterListIntent.HideDeleteDialog) },
                title = { Text("删除章节") },
                text = { Text("确定要删除「${state.chapterToDelete!!.title}」吗？") },
                confirmButton = {
                    TextButton(onClick = { viewModel.processIntent(ChapterListIntent.ConfirmDelete) }) {
                        Text("删除", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.processIntent(ChapterListIntent.HideDeleteDialog) }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterItem(
    chapter: Chapter,
    onClick: () -> Unit,
    onDelete: () -> Unit
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
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun getChapterStatusText(status: ChapterStatus): String {
    return when (status) {
        ChapterStatus.DRAFT -> "草稿"
        ChapterStatus.PUBLISHED -> "已发"
        ChapterStatus.ARCHIVED -> "已归"
    }
}
