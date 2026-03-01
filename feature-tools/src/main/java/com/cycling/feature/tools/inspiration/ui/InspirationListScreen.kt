package com.cycling.feature.tools.inspiration.ui

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
import androidx.compose.material.icons.filled.Lightbulb
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
import com.cycling.feature.tools.inspiration.InspirationListEffect
import com.cycling.feature.tools.inspiration.InspirationListIntent
import com.cycling.feature.tools.inspiration.InspirationListViewModel
import com.cycling.feature.tools.inspiration.ui.components.AddInspirationDialog
import com.cycling.feature.tools.inspiration.ui.components.DeleteInspirationDialog
import com.cycling.feature.tools.inspiration.ui.components.EditInspirationDialog
import com.cycling.feature.tools.inspiration.ui.components.InspirationItemCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspirationListScreen(
    onNavigateBack: () -> Unit,
    viewModel: InspirationListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is InspirationListEffect.ShowToast -> {
                }
                is InspirationListEffect.ShowError -> {
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("灵感收集") },
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
                onClick = { viewModel.onIntent(InspirationListIntent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加灵感")
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
                onValueChange = { viewModel.onIntent(InspirationListIntent.SearchInspirations(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索灵感标题、内容或标签") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                },
                singleLine = true
            )

            if (state.allTags.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item(key = "all") {
                        FilterChip(
                            selected = state.selectedTag == null,
                            onClick = { viewModel.onIntent(InspirationListIntent.FilterByTag(null)) },
                            label = { Text("全部") }
                        )
                    }
                    items(state.allTags, key = { it }) { tag ->
                        FilterChip(
                            selected = state.selectedTag == tag,
                            onClick = { viewModel.onIntent(InspirationListIntent.FilterByTag(tag)) },
                            label = { Text(tag) }
                        )
                    }
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
                    EmptyInspirationList(
                        searchQuery = state.searchQuery,
                        selectedTag = state.selectedTag
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displayInspirations, key = { it.id }) { inspiration ->
                            InspirationItemCard(
                                inspiration = inspiration,
                                onEdit = {
                                    viewModel.onIntent(InspirationListIntent.ShowEditDialog(inspiration))
                                },
                                onDelete = {
                                    viewModel.onIntent(InspirationListIntent.ShowDeleteDialog(inspiration))
                                }
                            )
                        }
                    }
                }
            }
        }

        if (state.showAddDialog) {
            AddInspirationDialog(
                onDismiss = { viewModel.onIntent(InspirationListIntent.HideAddDialog) },
                onConfirm = { inspiration ->
                    viewModel.onIntent(InspirationListIntent.AddInspiration(inspiration))
                }
            )
        }

        if (state.showEditDialog && state.inspirationToEdit != null) {
            EditInspirationDialog(
                inspiration = state.inspirationToEdit!!,
                onDismiss = { viewModel.onIntent(InspirationListIntent.HideEditDialog) },
                onConfirm = { inspiration ->
                    viewModel.onIntent(InspirationListIntent.UpdateInspiration(inspiration))
                }
            )
        }

        if (state.showDeleteDialog && state.inspirationToDelete != null) {
            DeleteInspirationDialog(
                inspiration = state.inspirationToDelete!!,
                onDismiss = { viewModel.onIntent(InspirationListIntent.HideDeleteDialog) },
                onConfirm = { viewModel.onIntent(InspirationListIntent.ConfirmDelete) }
            )
        }
    }
}

@Composable
private fun EmptyInspirationList(
    searchQuery: String,
    selectedTag: String?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (searchQuery.isNotBlank() || selectedTag != null) {
                Text(
                    text = "未找到匹配的灵感",
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
                    text = "还没有灵感",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击右下角按钮记录灵感",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "快速记录灵感，支持标签分类",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
