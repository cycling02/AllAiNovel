package com.cycling.feature.character.ui

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.core.ui.components.InputBottomSheet
import com.cycling.core.ui.components.SwipeToDeleteContainer
import com.cycling.domain.model.Character
import com.cycling.feature.character.CharacterListEffect
import com.cycling.feature.character.CharacterListIntent
import com.cycling.feature.character.CharacterListViewModel
import com.cycling.feature.character.ui.components.AiCharacterPreviewDialog
import com.cycling.feature.character.ui.components.AiGenerateCharacterDialog
import com.cycling.feature.character.ui.components.CharacterItemCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    bookId: Long,
    onNavigateBack: () -> Unit,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is CharacterListEffect.ShowToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                is CharacterListEffect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                is CharacterListEffect.ShowUndoSnackbar -> {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = "撤销",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onIntent(CharacterListIntent.UndoDelete(effect.character))
                        }
                    }
                }
                is CharacterListEffect.NavigateToDetail -> {
                }
                CharacterListEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("角色管理") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(CharacterListIntent.ShowAiGenerateDialog) }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI生成")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(CharacterListIntent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加角色")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onIntent(CharacterListIntent.SearchCharacters(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索角色姓名或别名") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "搜索")
                },
                singleLine = true
            )

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
                    EmptyCharacterList(
                        searchQuery = state.searchQuery,
                        onAiGenerateClick = { viewModel.onIntent(CharacterListIntent.ShowAiGenerateDialog) }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.displayCharacters, key = { it.id }) { character ->
                            SwipeToDeleteCharacterItem(
                                character = character,
                                onClick = { viewModel.onIntent(CharacterListIntent.ShowEditDialog(character)) },
                                onDelete = { viewModel.onIntent(CharacterListIntent.DeleteCharacter(character)) }
                            )
                        }
                    }
                }
            }
        }

        if (state.showAddDialog) {
            AddCharacterBottomSheet(
                bookId = bookId,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideAddDialog) },
                onConfirm = { character ->
                    viewModel.onIntent(CharacterListIntent.AddCharacter(character))
                }
            )
        }

        if (state.showEditDialog && state.characterToEdit != null) {
            EditCharacterBottomSheet(
                character = state.characterToEdit!!,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideEditDialog) },
                onConfirm = { character ->
                    viewModel.onIntent(CharacterListIntent.UpdateCharacter(character))
                }
            )
        }

        if (state.showAiGenerateDialog) {
            AiGenerateCharacterDialog(
                isLoading = state.isAiGenerating,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideAiGenerateDialog) },
                onGenerate = { characterType, gender, description, count ->
                    viewModel.onIntent(
                        CharacterListIntent.GenerateCharacter(
                            characterType,
                            gender,
                            description,
                            count
                        )
                    )
                }
            )
        }

        if (state.showAiPreviewDialog && state.aiGeneratedCharacters != null) {
            AiCharacterPreviewDialog(
                characters = state.aiGeneratedCharacters!!,
                onDismiss = { viewModel.onIntent(CharacterListIntent.HideAiPreviewDialog) },
                onApply = { viewModel.onIntent(CharacterListIntent.ApplyAiCharacters) }
            )
        }
    }
}

@Composable
private fun SwipeToDeleteCharacterItem(
    character: Character,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    SwipeToDeleteContainer(
        item = character,
        onDelete = onDelete
    ) {
        CharacterItemCard(
            character = character,
            onEdit = onClick,
            onDelete = {}
        )
    }
}

@Composable
private fun EmptyCharacterList(
    searchQuery: String,
    onAiGenerateClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (searchQuery.isNotBlank()) {
                Text(
                    text = "未找到匹配的角色",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "尝试其他关键词",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            } else {
                Text(
                    text = "还没有角色",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击右下角按钮创建角色",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onAiGenerateClick,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI 生成角色")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "创建角色档案，记录姓名、性格、外貌等信息",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
