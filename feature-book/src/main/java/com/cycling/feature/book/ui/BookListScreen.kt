package com.cycling.feature.book.ui

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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.cycling.core.ui.components.SwipeToDeleteContainer
import com.cycling.domain.model.Book
import com.cycling.feature.book.BookListEffect
import com.cycling.feature.book.BookListIntent
import com.cycling.feature.book.BookListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onNavigateToChapters: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToTools: () -> Unit,
    onNavigateToOneClickGeneration: () -> Unit,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var newBookTitle by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookListEffect.NavigateToChapters -> {
                    onNavigateToChapters(effect.bookId)
                }
                BookListEffect.NavigateToSettings -> {
                    onNavigateToSettings()
                }
                is BookListEffect.ShowToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                is BookListEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.error)
                    }
                }
                is BookListEffect.ShowUndoSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = "撤销",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onIntent(BookListIntent.UndoDelete(effect.book))
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的小说") },
                actions = {
                    IconButton(onClick = onNavigateToOneClickGeneration) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "一键生成")
                    }
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                    IconButton(onClick = onNavigateToTools) {
                        Icon(Icons.Default.Build, contentDescription = "工具")
                    }
                    IconButton(onClick = onNavigateToStatistics) {
                        Icon(Icons.Default.BarChart, contentDescription = "统计")
                    }
                    IconButton(onClick = { viewModel.navigateToSettings() }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(BookListIntent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加书籍")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showSearchBar) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onIntent(BookListIntent.SearchBooks(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("搜索书籍...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onIntent(BookListIntent.SearchBooks("")) }) {
                                Icon(Icons.Default.Close, contentDescription = "清除")
                            }
                        }
                    },
                    singleLine = true
                )
            }

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
                    EmptyBookList()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displayBooks, key = { it.id }) { book ->
                            SwipeToDeleteBookItem(
                                book = book,
                                onClick = { viewModel.navigateToChapters(book.id) },
                                onDelete = { viewModel.onIntent(BookListIntent.DeleteBook(book)) }
                            )
                        }
                    }
                }
            }
        }

        InputBottomSheet(
            visible = state.showAddDialog,
            title = "创建新书",
            onDismiss = {
                viewModel.onIntent(BookListIntent.HideAddDialog)
                newBookTitle = ""
            },
            confirmText = "创建",
            confirmEnabled = newBookTitle.isNotBlank(),
            onConfirm = {
                viewModel.onIntent(BookListIntent.AddBook(newBookTitle))
                newBookTitle = ""
            }
        ) {
            OutlinedTextField(
                value = newBookTitle,
                onValueChange = { newBookTitle = it },
                label = { Text("书名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SwipeToDeleteBookItem(
    book: Book,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    SwipeToDeleteContainer(
        item = book,
        onDelete = onDelete
    ) {
        BookCard(book = book, onClick = onClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (book.wordCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${book.wordCount}字",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyBookList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "还没有书",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "点击右下角按钮创建新书",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
