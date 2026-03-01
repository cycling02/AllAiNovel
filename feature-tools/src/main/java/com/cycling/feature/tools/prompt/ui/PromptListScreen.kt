package com.cycling.feature.tools.prompt.ui

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.cycling.domain.model.PromptCategory
import com.cycling.feature.tools.prompt.PromptEffect
import com.cycling.feature.tools.prompt.PromptIntent
import com.cycling.feature.tools.prompt.PromptViewModel
import com.cycling.feature.tools.prompt.ui.components.DeleteConfirmDialog
import com.cycling.feature.tools.prompt.ui.components.PromptCard
import com.cycling.feature.tools.prompt.ui.components.PromptEditDialog
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptListScreen(
    onNavigateBack: () -> Unit,
    viewModel: PromptViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is PromptEffect.ShowToast -> {
                }
                is PromptEffect.ShowError -> {
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("提示词管理") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(PromptIntent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加提示词")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onIntent(PromptIntent.SearchPrompts(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索提示词名称或内容") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                },
                singleLine = true
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = "all") {
                    FilterChip(
                        selected = state.selectedCategory == null && !state.showFavoritesOnly,
                        onClick = {
                            viewModel.onIntent(PromptIntent.SelectCategory(null))
                            if (state.showFavoritesOnly) {
                                viewModel.onIntent(PromptIntent.ToggleFavoritesFilter)
                            }
                        },
                        label = { Text("全部") }
                    )
                }
                item(key = "favorites") {
                    FilterChip(
                        selected = state.showFavoritesOnly,
                        onClick = { viewModel.onIntent(PromptIntent.ToggleFavoritesFilter) },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (state.showFavoritesOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("收藏")
                            }
                        }
                    )
                }
                items(PromptCategory.entries, key = { it.name }) { category ->
                    FilterChip(
                        selected = state.selectedCategory == category,
                        onClick = { viewModel.onIntent(PromptIntent.SelectCategory(category)) },
                        label = { Text(category.displayName) }
                    )
                }
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
                    EmptyPromptList(
                        searchQuery = state.searchQuery,
                        selectedCategory = state.selectedCategory,
                        showFavoritesOnly = state.showFavoritesOnly
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displayPrompts, key = { it.id }) { prompt ->
                            PromptCard(
                                prompt = prompt,
                                onEdit = {
                                    viewModel.onIntent(PromptIntent.ShowEditDialog(prompt))
                                },
                                onDelete = {
                                    viewModel.onIntent(PromptIntent.ShowDeleteConfirm(prompt))
                                },
                                onToggleFavorite = {
                                    viewModel.onIntent(PromptIntent.ToggleFavorite(prompt))
                                }
                            )
                        }
                    }
                }
            }
        }

        if (state.showAddDialog) {
            PromptEditDialog(
                onDismiss = { viewModel.onIntent(PromptIntent.HideAddDialog) },
                onConfirm = { prompt ->
                    viewModel.onIntent(PromptIntent.CreatePrompt(prompt))
                }
            )
        }

        if (state.showEditDialog && state.editingPrompt != null) {
            PromptEditDialog(
                prompt = state.editingPrompt,
                onDismiss = { viewModel.onIntent(PromptIntent.HideEditDialog) },
                onConfirm = { prompt ->
                    viewModel.onIntent(PromptIntent.UpdatePrompt(prompt))
                }
            )
        }

        if (state.showDeleteConfirm) {
            val promptToDelete = state.promptToDelete
            if (promptToDelete != null) {
                DeleteConfirmDialog(
                    prompt = promptToDelete,
                    onDismiss = { viewModel.onIntent(PromptIntent.HideDeleteConfirm) },
                    onConfirm = {
                        viewModel.onIntent(PromptIntent.DeletePrompt(promptToDelete))
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyPromptList(
    searchQuery: String,
    selectedCategory: PromptCategory?,
    showFavoritesOnly: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Article,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (searchQuery.isNotBlank() || selectedCategory != null || showFavoritesOnly) {
                Text(
                    text = "未找到匹配的提示词",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "尝试其他关键词或清除筛选",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                Text(
                    text = "还没有提示词",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击右下角按钮添加提示词",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "管理AI写作提示词模板",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
