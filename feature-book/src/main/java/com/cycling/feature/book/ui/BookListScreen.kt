package com.cycling.feature.book.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.feature.book.BookListEffect
import com.cycling.feature.book.BookListIntent
import com.cycling.feature.book.BookListViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onNavigateToChapters: (Long) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect
    
    var newBookTitle by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    
    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is BookListEffect.NavigateToChapters -> {
                    onNavigateToChapters(effect.bookId)
                }
                BookListEffect.NavigateToSettings -> {
                    onNavigateToSettings()
                }
                is BookListEffect.ShowToast -> {
                }
                is BookListEffect.ShowError -> {
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的小说") },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
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
        }
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
                            BookItem(
                                book = book,
                                onClick = { viewModel.navigateToChapters(book.id) },
                                onDelete = { viewModel.onIntent(BookListIntent.ShowDeleteDialog(book)) }
                            )
                        }
                    }
                }
            }
        }
        
        if (state.showAddDialog) {
            AddBookDialog(
                title = newBookTitle,
                onTitleChange = { newBookTitle = it },
                onDismiss = {
                    viewModel.onIntent(BookListIntent.HideAddDialog)
                    newBookTitle = ""
                },
                onConfirm = {
                    if (newBookTitle.isNotBlank()) {
                        viewModel.onIntent(BookListIntent.AddBook(newBookTitle))
                        newBookTitle = ""
                    }
                }
            )
        }
        
        if (state.showDeleteDialog && state.bookToDelete != null) {
            DeleteBookDialog(
                bookTitle = state.bookToDelete!!.title,
                onDismiss = { viewModel.onIntent(BookListIntent.HideDeleteDialog) },
                onConfirm = { viewModel.onIntent(BookListIntent.ConfirmDelete) }
            )
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

@Composable
private fun AddBookDialog(
    title: String,
    onTitleChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("创建新书") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("书名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = title.isNotBlank()
            ) {
                Text("创建")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun DeleteBookDialog(
    bookTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("删除书籍") },
        text = { Text("确定要删除《$bookTitle》吗？这将删除该书的所有章节。") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("删除", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}