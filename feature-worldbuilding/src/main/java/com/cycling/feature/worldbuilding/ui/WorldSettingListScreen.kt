package com.cycling.feature.worldbuilding.ui

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
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.domain.model.SettingType
import com.cycling.feature.worldbuilding.WorldSettingListEffect
import com.cycling.feature.worldbuilding.WorldSettingListIntent
import com.cycling.feature.worldbuilding.WorldSettingListViewModel
import com.cycling.feature.worldbuilding.ui.components.AddWorldSettingDialog
import com.cycling.feature.worldbuilding.ui.components.DeleteWorldSettingDialog
import com.cycling.feature.worldbuilding.ui.components.EditWorldSettingDialog
import com.cycling.feature.worldbuilding.ui.components.WorldSettingItemCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldSettingListScreen(
    bookId: Long,
    onNavigateBack: () -> Unit,
    viewModel: WorldSettingListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is WorldSettingListEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is WorldSettingListEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                WorldSettingListEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("世界观设定") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
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
                onClick = { viewModel.onIntent(WorldSettingListIntent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加设定")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onIntent(WorldSettingListIntent.SearchSettings(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索设定名称或描述") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                },
                singleLine = true
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item(key = "filter_icon") {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = "筛选",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                item(key = "all") {
                    FilterChip(
                        onClick = { viewModel.onIntent(WorldSettingListIntent.FilterByType(null)) },
                        label = { Text("全部") },
                        selected = state.selectedType == null
                    )
                }
                
                items(SettingType.entries, key = { it.name }) { type ->
                    FilterChip(
                        onClick = { viewModel.onIntent(WorldSettingListIntent.FilterByType(type)) },
                        label = { Text(type.displayName) },
                        selected = state.selectedType == type
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
                    EmptyWorldSettingList(
                        searchQuery = state.searchQuery,
                        selectedType = state.selectedType
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displaySettings, key = { it.id }) { setting ->
                            WorldSettingItemCard(
                                setting = setting,
                                onEdit = {
                                    viewModel.onIntent(WorldSettingListIntent.ShowEditDialog(setting))
                                },
                                onDelete = {
                                    viewModel.onIntent(WorldSettingListIntent.ShowDeleteDialog(setting))
                                }
                            )
                        }
                    }
                }
            }
        }
        
        if (state.showAddDialog) {
            AddWorldSettingDialog(
                bookId = bookId,
                onDismiss = { viewModel.onIntent(WorldSettingListIntent.HideAddDialog) },
                onConfirm = { setting ->
                    viewModel.onIntent(WorldSettingListIntent.AddSetting(setting))
                }
            )
        }
        
        if (state.showEditDialog && state.settingToEdit != null) {
            EditWorldSettingDialog(
                setting = state.settingToEdit!!,
                onDismiss = { viewModel.onIntent(WorldSettingListIntent.HideEditDialog) },
                onConfirm = { setting ->
                    viewModel.onIntent(WorldSettingListIntent.UpdateSetting(setting))
                }
            )
        }
        
        if (state.showDeleteDialog && state.settingToDelete != null) {
            DeleteWorldSettingDialog(
                setting = state.settingToDelete!!,
                onDismiss = { viewModel.onIntent(WorldSettingListIntent.HideDeleteDialog) },
                onConfirm = { viewModel.onIntent(WorldSettingListIntent.ConfirmDelete) }
            )
        }
    }
}

@Composable
private fun EmptyWorldSettingList(
    searchQuery: String,
    selectedType: SettingType?
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            when {
                searchQuery.isNotBlank() -> {
                    Text(
                        text = "未找到匹配的设定",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "尝试其他关键词",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                selectedType != null -> {
                    Text(
                        text = "没有${selectedType.displayName}类型的设定",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角按钮创建设定",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                else -> {
                    Text(
                        text = "还没有世界观设定",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击右下角按钮创建设定",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "支持地点、势力、力量体系、物品四种类型",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}
