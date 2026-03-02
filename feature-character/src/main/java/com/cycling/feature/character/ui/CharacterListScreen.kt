package com.cycling.feature.character.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.*
import com.cycling.domain.model.Character
import com.cycling.feature.character.CharacterListEffect
import com.cycling.feature.character.CharacterListIntent
import com.cycling.feature.character.CharacterListViewModel
import com.cycling.feature.character.ui.components.*
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    bookId: Long,
    onNavigateBack: () -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CharacterListEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is CharacterListEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                CharacterListEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is CharacterListEffect.NavigateToDetail -> {
                    // TODO: Navigate to character detail
                }
                is CharacterListEffect.ShowUndoSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = "撤销"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onIntent(CharacterListIntent.UndoDelete(effect.character))
                    }
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            IOSNavBar(
                title = "角色管理",
                onBack = { viewModel.navigateBack() }
            )
        },
        floatingActionButton = {
            IOSFAB(
                onClick = { viewModel.onIntent(CharacterListIntent.ShowAddDialog) },
                text = "新增角色"
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        when {
            state.isLoading -> {
                IOSFullScreenLoading()
            }
            state.characters.isEmpty() -> {
                IOSEmptyState(
                    icon = Icons.Outlined.Person,
                    title = "还没有角色",
                    message = "创建角色档案帮助你塑造人物",
                    modifier = Modifier.padding(padding),
                    action = {
                        IOSButton(
                            text = "新增角色",
                            onClick = { viewModel.onIntent(CharacterListIntent.ShowAddDialog) },
                            icon = Icons.Default.Add,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = IOSSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(IOSSpacing.sm)
                ) {
                    item {
                        IOSSectionHeader(title = "角色列表")
                    }
                    
                    items(state.characters, key = { it.id }) { character ->
                        CharacterItemCard(
                            character = character,
                            onEdit = { viewModel.onIntent(CharacterListIntent.ShowEditDialog(character)) },
                            onDelete = { viewModel.onIntent(CharacterListIntent.ShowDeleteDialog(character)) }
                        )
                    }
                    
                    item {
                        IOSSpacer(height = IOSSpacing.xxxl)
                    }
                }
            }
        }
        
        if (state.showAddDialog) {
            AddCharacterDialog(
                bookId = bookId,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideAddDialog) },
                onConfirm = { character ->
                    viewModel.onIntent(CharacterListIntent.AddCharacter(character))
                }
            )
        }
        
        if (state.showEditDialog && state.characterToEdit != null) {
            EditCharacterDialog(
                character = state.characterToEdit!!,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideEditDialog) },
                onConfirm = { character ->
                    viewModel.onIntent(CharacterListIntent.UpdateCharacter(character))
                }
            )
        }
        
        if (state.showDeleteDialog && state.characterToDelete != null) {
            IOSConfirmDialog(
                visible = true,
                title = "删除角色",
                message = "确定要删除「${state.characterToDelete!!.name}」吗？",
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideDeleteDialog) },
                onConfirm = { viewModel.onIntent(CharacterListIntent.ConfirmDelete) },
                isDestructive = true
            )
        }
    }
}

@Composable
private fun CharacterItemCard(
    character: Character,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    IOSListItemCard(
        icon = Icons.Outlined.Person,
        iconBackground = when (character.gender) {
            "男" -> MaterialTheme.colorScheme.primary
            "女" -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.tertiary
        },
        title = character.name,
        subtitle = character.personality.take(50),
        badge = character.gender.takeIf { it.isNotBlank() },
        onClick = onEdit,
        trailing = {
            IOSIconButton(onClick = onDelete, icon = Icons.Default.Delete, tint = MaterialTheme.colorScheme.error)
        }
    )
}
