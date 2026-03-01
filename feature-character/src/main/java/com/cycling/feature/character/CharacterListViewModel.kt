package com.cycling.feature.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cycling.domain.model.Character
import com.cycling.domain.usecase.ai.GenerateCharacterUseCase
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.character.AddCharacterUseCase
import com.cycling.domain.usecase.character.DeleteCharacterUseCase
import com.cycling.domain.usecase.character.GetCharactersByBookIdUseCase
import com.cycling.domain.usecase.character.UpdateCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    getCharactersByBookIdUseCase: GetCharactersByBookIdUseCase,
    private val addCharacterUseCase: AddCharacterUseCase,
    private val updateCharacterUseCase: UpdateCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase,
    private val generateCharacterUseCase: GenerateCharacterUseCase,
    private val getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: Long = savedStateHandle["bookId"] ?: 0L

    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CharacterListEffect>()
    val effect = _effect.asSharedFlow()

    private val charactersFlow = getCharactersByBookIdUseCase(bookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private var recentlyDeletedCharacter: Character? = null

    init {
        viewModelScope.launch {
            charactersFlow.collect { characters ->
                val currentQuery = _state.value.searchQuery
                _state.value = _state.value.copy(
                    characters = characters,
                    filteredCharacters = if (currentQuery.isNotBlank()) {
                        characters.filter { it.name.contains(currentQuery, ignoreCase = true) }
                    } else {
                        emptyList()
                    },
                    isLoading = false
                )
            }
        }
    }

    fun onIntent(intent: CharacterListIntent) {
        when (intent) {
            CharacterListIntent.LoadCharacters -> loadCharacters()
            CharacterListIntent.ShowAddDialog -> showAddDialog()
            CharacterListIntent.HideAddDialog -> hideAddDialog()
            is CharacterListIntent.ShowEditDialog -> showEditDialog(intent.character)
            CharacterListIntent.HideEditDialog -> hideEditDialog()
            is CharacterListIntent.DeleteCharacter -> deleteCharacter(intent.character)
            is CharacterListIntent.UndoDelete -> undoDelete(intent.character)
            is CharacterListIntent.AddCharacter -> addCharacter(intent.character)
            is CharacterListIntent.UpdateCharacter -> updateCharacter(intent.character)
            is CharacterListIntent.SearchCharacters -> searchCharacters(intent.query)
            CharacterListIntent.ShowAiGenerateDialog -> showAiGenerateDialog()
            CharacterListIntent.HideAiGenerateDialog -> hideAiGenerateDialog()
            is CharacterListIntent.GenerateCharacter -> generateCharacter(
                intent.characterType,
                intent.gender,
                intent.description,
                intent.count
            )
            CharacterListIntent.ApplyAiCharacters -> applyAiCharacters()
            CharacterListIntent.HideAiPreviewDialog -> hideAiPreviewDialog()
        }
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
        }
    }

    private fun showAddDialog() {
        _state.value = _state.value.copy(showAddDialog = true)
    }

    private fun hideAddDialog() {
        _state.value = _state.value.copy(showAddDialog = false)
    }

    private fun showEditDialog(character: Character) {
        _state.value = _state.value.copy(
            showEditDialog = true,
            characterToEdit = character
        )
    }

    private fun hideEditDialog() {
        _state.value = _state.value.copy(
            showEditDialog = false,
            characterToEdit = null
        )
    }

    private fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            try {
                recentlyDeletedCharacter = character
                deleteCharacterUseCase(character.id)
                _effect.emit(CharacterListEffect.ShowUndoSnackbar("「${character.name}」已删除", character))
            } catch (e: IOException) {
                _state.value = _state.value.copy(error = e.message)
                _effect.emit(CharacterListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(error = e.message)
                _effect.emit(CharacterListEffect.ShowError("删除角色失败: ${e.message}"))
            }
        }
    }

    private fun undoDelete(character: Character) {
        viewModelScope.launch {
            try {
                val restoredCharacter = character.copy(id = 0)
                addCharacterUseCase(restoredCharacter)
                recentlyDeletedCharacter = null
                _effect.emit(CharacterListEffect.ShowToast("已恢复"))
            } catch (e: RuntimeException) {
                _effect.emit(CharacterListEffect.ShowError("恢复角色失败: ${e.message}"))
            }
        }
    }

    private fun addCharacter(character: Character) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                addCharacterUseCase(character)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAddDialog = false
                )
                _effect.emit(CharacterListEffect.ShowToast("角色创建成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(CharacterListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(CharacterListEffect.ShowError("创建角色失败: ${e.message}"))
            }
        }
    }

    private fun updateCharacter(character: Character) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val updatedCharacter = character.copy(
                    updatedAt = System.currentTimeMillis()
                )
                updateCharacterUseCase(updatedCharacter)
                _state.value = _state.value.copy(
                    isLoading = false,
                    showEditDialog = false,
                    characterToEdit = null
                )
                _effect.emit(CharacterListEffect.ShowToast("角色更新成功"))
            } catch (e: IOException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(CharacterListEffect.ShowError("网络错误: ${e.message}"))
            } catch (e: RuntimeException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(CharacterListEffect.ShowError("更新角色失败: ${e.message}"))
            }
        }
    }

    private fun searchCharacters(query: String) {
        val filtered = if (query.isNotBlank()) {
            _state.value.characters.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.alias.contains(query, ignoreCase = true)
            }
        } else {
            emptyList()
        }
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredCharacters = filtered
        )
    }

    fun navigateBack() {
        viewModelScope.launch {
            _effect.emit(CharacterListEffect.NavigateBack)
        }
    }

    private fun showAiGenerateDialog() {
        _state.value = _state.value.copy(showAiGenerateDialog = true)
    }

    private fun hideAiGenerateDialog() {
        _state.value = _state.value.copy(showAiGenerateDialog = false)
    }

    private fun generateCharacter(
        characterType: String?,
        gender: String?,
        description: String?,
        count: Int
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isAiGenerating = true,
                showAiGenerateDialog = false
            )

            val defaultConfig = getDefaultApiConfigUseCase().first()
            if (defaultConfig == null) {
                _state.value = _state.value.copy(
                    isAiGenerating = false,
                    error = "请先配置 AI API"
                )
                _effect.emit(CharacterListEffect.ShowError("请先配置 AI API"))
                return@launch
            }

            val result = generateCharacterUseCase(
                config = defaultConfig,
                bookId = bookId,
                characterType = characterType,
                gender = gender,
                description = description,
                count = count
            )

            result.fold(
                onSuccess = { characters ->
                    _state.value = _state.value.copy(
                        isAiGenerating = false,
                        aiGeneratedCharacters = characters,
                        showAiPreviewDialog = true
                    )
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isAiGenerating = false,
                        error = exception.message
                    )
                    _effect.emit(CharacterListEffect.ShowError("生成失败: ${exception.message}"))
                }
            )
        }
    }

    private fun applyAiCharacters() {
        viewModelScope.launch {
            val characters = _state.value.aiGeneratedCharacters ?: return@launch

            _state.value = _state.value.copy(isLoading = true)

            try {
                characters.forEach { character ->
                    addCharacterUseCase(character)
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    showAiPreviewDialog = false,
                    aiGeneratedCharacters = null
                )
                _effect.emit(CharacterListEffect.ShowToast("成功添加 ${characters.size} 个角色"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _effect.emit(CharacterListEffect.ShowError("保存角色失败: ${e.message}"))
            }
        }
    }

    private fun hideAiPreviewDialog() {
        _state.value = _state.value.copy(
            showAiPreviewDialog = false,
            aiGeneratedCharacters = null
        )
    }
}
