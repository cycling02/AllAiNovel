package com.cycling.feature.worldbuilding.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.SettingType
import com.cycling.feature.worldbuilding.WorldSettingListEffect
import com.cycling.feature.worldbuilding.WorldSettingListIntent
import com.cycling.feature.worldbuilding.WorldSettingListViewModel
import com.cycling.feature.worldbuilding.ui.components.*
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
            IOSNavBar(
                title = "世界观设定",
                onBack = { viewModel.navigateBack() }
            )
        },
        floatingActionButton = {
            IOSFAB(
                onClick = { viewModel.onIntent(WorldSettingListIntent.ShowAddDialog) },
                text = "新建设定"
            )
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
            IOSSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onIntent(WorldSettingListIntent.SearchSettings(it)) },
                modifier = Modifier.padding(vertical = IOSSpacing.sm)
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = IOSSpacing.lg, vertical = IOSSpacing.xs),
                horizontalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
            ) {
                item(key = "all") {
                    IOSCompactButton(
                        text = "全部",
                        onClick = { viewModel.onIntent(WorldSettingListIntent.FilterByType(null)) },
                        style = if (state.selectedType == null) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                }
                
                items(SettingType.entries, key = { it.name }) { type ->
                    IOSCompactButton(
                        text = type.displayName,
                        onClick = { viewModel.onIntent(WorldSettingListIntent.FilterByType(type)) },
                        style = if (state.selectedType == type) IOSButtonStyle.Primary else IOSButtonStyle.Secondary
                    )
                }
            }
            
            when {
                state.isLoading -> {
                    IOSFullScreenLoading()
                }
                state.isEmpty -> {
                    EmptyWorldSettingList(
                        searchQuery = state.searchQuery,
                        selectedType = state.selectedType,
                        onAddClick = { viewModel.onIntent(WorldSettingListIntent.ShowAddDialog) }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = IOSSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
                    ) {
                        item {
                            IOSSectionHeader(title = "设定列表")
                        }
                        
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
                        
                        item {
                            IOSSpacer(height = IOSSpacing.xxxl)
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
    selectedType: SettingType?,
    onAddClick: () -> Unit
) {
    IOSEmptyState(
        icon = Icons.Outlined.Public,
        title = when {
            searchQuery.isNotBlank() -> "未找到匹配的设定"
            selectedType != null -> "没有${selectedType.displayName}类型的设定"
            else -> "还没有世界观设定"
        },
        message = when {
            searchQuery.isNotBlank() -> "尝试其他关键词"
            selectedType != null -> "点击右下角按钮创建设定"
            else -> "支持地点、势力、力量体系、物品四种类型"
        },
        action = if (searchQuery.isBlank() && selectedType == null) {
            {
                IOSButton(
                    text = "新建设定",
                    onClick = onAddClick,
                    icon = Icons.Default.Add,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        } else null
    )
}
