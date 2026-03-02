package com.cycling.feature.book.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.Book
import com.cycling.feature.book.BookListEffect
import com.cycling.feature.book.BookListIntent
import com.cycling.feature.book.BookListViewModel
import kotlinx.coroutines.launch

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
    var searchQuery by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookListEffect.NavigateToChapters -> onNavigateToChapters(effect.bookId)
                BookListEffect.NavigateToSettings -> onNavigateToSettings()
                is BookListEffect.ShowToast -> {
                    scope.launch { snackbarHostState.showSnackbar(effect.message) }
                }
                is BookListEffect.ShowError -> {
                    scope.launch { snackbarHostState.showSnackbar(effect.error) }
                }
                is BookListEffect.ShowUndoSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = "撤销",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onIntent(BookListIntent.UndoDelete(effect.book))
                        }
                    }
                }
            }
        }
    }

    // 底部导航栏页面不使用 Scaffold，由 AppNavigation 统一管理
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部栏
        Column {
            IOSLargeNavBar(
                title = "我的小说",
                actions = {
                    IconButton(onClick = onNavigateToOneClickGeneration) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "一键生成")
                    }
                }
            )
            AnimatedVisibility(
                visible = showSearchBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IOSSearchBar(
                    query = searchQuery,
                    onQueryChange = { 
                        searchQuery = it
                        viewModel.onIntent(BookListIntent.SearchBooks(it))
                    }
                )
            }
        }

        // 内容区域
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    IOSFullScreenLoading()
                }
                state.isEmpty -> {
                    IOSEmptyState(
                        icon = Icons.Outlined.Book,
                        title = "还没有书籍",
                        message = "创建你的第一本小说，开始创作之旅",
                        action = {
                            IOSButton(
                                text = "一键生成",
                                onClick = onNavigateToOneClickGeneration,
                                icon = Icons.Default.AutoAwesome,
                                modifier = Modifier.fillMaxWidth(0.6f)
                            )
                        },
                        secondaryAction = {
                            IOSButton(
                                text = "手动创建",
                                onClick = { viewModel.onIntent(BookListIntent.ShowAddDialog) },
                                style = IOSButtonStyle.Secondary,
                                icon = Icons.Default.Add,
                                modifier = Modifier.fillMaxWidth(0.6f)
                            )
                        }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = IOSSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(IOSSpacing.md)
                    ) {
                        item {
                            IOSSectionHeader(title = "全部书籍")
                        }
                        
                        items(state.displayBooks, key = { it.id }) { book ->
                            BookItem(
                                book = book,
                                onClick = { viewModel.navigateToChapters(book.id) },
                                onDelete = { viewModel.onIntent(BookListIntent.DeleteBook(book)) }
                            )
                        }
                        
                        iosSpacer(height = IOSSpacing.xl)
                    }
                }
            }

            // FAB
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(IOSSpacing.lg)
            ) {
                IOSFAB(onClick = { viewModel.onIntent(BookListIntent.ShowAddDialog) })
            }
        }
    }

    IOSInputDialog(
        visible = state.showAddDialog,
        title = "创建新书",
        onDismiss = {
            viewModel.onIntent(BookListIntent.HideAddDialog)
            newBookTitle = ""
        },
        onConfirm = {
            viewModel.onIntent(BookListIntent.AddBook(it))
        },
        placeholder = "输入书名",
        label = "书名",
        confirmText = "创建",
        dismissText = "取消"
    )
}

@Composable
private fun BookItem(
    book: Book,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    IOSSwipeToDelete(
        item = book,
        onDelete = onDelete
    ) {
        IOSListItemCard(
            icon = Icons.Outlined.Book,
            title = book.title,
            subtitle = if (book.wordCount > 0) "${book.wordCount}字" else null,
            onClick = onClick
        )
    }
}
